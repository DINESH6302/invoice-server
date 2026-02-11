package com.invoice.service;

import com.invoice.context.OrgContext;
import com.invoice.dto.InvoiceCreationRequestDto;
import com.invoice.dto.InvoiceResponseDto;
import com.invoice.dto.InvoiceSummary;
import com.invoice.dto.InvoiceSummaryDto;
import com.invoice.dto.StatusUpdateDto;
import com.invoice.exception.NotFountException;
import com.invoice.models.Customer;
import com.invoice.models.Enum.InvoiceStatus;
import com.invoice.models.Invoice;
import com.invoice.models.Organization;
import com.invoice.models.Template;
import com.invoice.repositorie.CustomerRepository;
import com.invoice.repositorie.InvoiceRepository;
import com.invoice.repositorie.OrgRepository;
import com.invoice.repositorie.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final TemplateRepository templateRepository;
    private final OrgRepository orgRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, CustomerRepository customerRepository, TemplateRepository templateRepository, OrgRepository orgRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.templateRepository = templateRepository;
        this.orgRepository = orgRepository;
    }

    public List<InvoiceSummaryDto> getInvoicesSummary() {
        List<InvoiceSummary> invoiceList = invoiceRepository.findByOrganization_OrgId(OrgContext.getOrgId());

        return invoiceList.stream().map(e ->
                new InvoiceSummaryDto(
                        e.getInvoiceId(),
                        e.getTemplateId(),
                        e.getInvoiceNumber(),
                        e.getCustomerName(),
                        e.getInvoiceDate(),
                        e.getTotal(),
                        e.getInvoiceStatus()
                )).toList();
    }

    public InvoiceResponseDto getInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByInvoiceId_AndOrganization_OrgId(invoiceId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Invoice not found."));

        InvoiceResponseDto response = new InvoiceResponseDto();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setCustomerId(invoice.getCustomer().getCustomerId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setDate(invoice.getDate());
        response.setStatus(invoice.getInvoiceStatus());
        response.setTax(invoice.getTax());
        response.setTotal(invoice.getTotal());
        response.setQuantity(invoice.getQuantity());
        response.setHeader(invoice.getHeader());
        response.setCustomerDetails(invoice.getCustomerDetails());
        response.setInvoiceMeta(invoice.getInvoiceMeta());
        response.setItems(invoice.getItems());
        response.setSummary(invoice.getSummary());
        response.setFooter(invoice.getFooter());

        return response;
    }

    @Transactional
    public Long createInvoice(InvoiceCreationRequestDto requestDto) {
        Long customerId = requestDto.getCustomerId();
        Customer customer = customerRepository.findByCustomerIdAndOrganization_OrgId(customerId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Customer not found."));

        Template template = templateRepository.findByTemplateIdAndOrganization_OrgId(requestDto.getTemplateId(), OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("You don't have access to this template."));

        Organization org = orgRepository.findById(OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Organization not found."));

        String invoiceNumber = "";
        LocalDate date = LocalDate.now();

        ArrayList<Object> headerFields = (ArrayList<Object>) requestDto.getHeader().get("fields");
        for (Object fieldObj : headerFields) {
            Map<String, Object> field = (Map<String, Object>) fieldObj;
            if ("invoice no".equals(field.get("label").toString().toLowerCase().trim())) {
                invoiceNumber = (String) field.get("value");
            } else if ("date".equals(field.get("label").toString().toLowerCase().trim())) {
                date = LocalDate.parse((String) field.get("value"));
            }
        }

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setDate(date);
        invoice.setInvoiceStatus(InvoiceStatus.DRAFT);
        invoice.setOrganization(org);
        invoice.setTemplate(template);
        invoice.setTotal(requestDto.getTotal());
        invoice.setTax(requestDto.getTax());
        invoice.setQuantity(requestDto.getQuantity());
        invoice.setHeader(requestDto.getHeader());
        invoice.setCustomerDetails(requestDto.getCustomerDetails());
        invoice.setInvoiceMeta(requestDto.getInvoiceMeta());
        invoice.setItems(requestDto.getItems());
        invoice.setSummary(requestDto.getSummary());
        invoice.setFooter(requestDto.getFooter());

        invoiceRepository.save(invoice);
        return invoice.getInvoiceId();
    }

    @Transactional
    public void updateInvoice(Long invoiceId, InvoiceCreationRequestDto requestDto) {
        Invoice invoice = invoiceRepository.findByInvoiceId_AndOrganization_OrgId(invoiceId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Invoice not found."));

        Customer customer = customerRepository.findByCustomerIdAndOrganization_OrgId(requestDto.getCustomerId(), OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Customer not found."));

        Template template = templateRepository.findByTemplateIdAndOrganization_OrgId(requestDto.getTemplateId(), OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("You don't have access to this template."));

        Organization org = orgRepository.findById(OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Organization not found."));

        String invoiceNumber = "";
        LocalDate date = LocalDate.now();

        ArrayList<Object> headerFields = (ArrayList<Object>) requestDto.getHeader().get("fields");
        for (Object fieldObj : headerFields) {
            Map<String, Object> field = (Map<String, Object>) fieldObj;
            if ("invoice no".equals(field.get("label").toString().toLowerCase().trim())) {
                invoiceNumber = (String) field.get("value");
            } else if ("date".equals(field.get("label").toString().toLowerCase().trim())) {
                date = LocalDate.parse((String) field.get("value"));
            }
        }

        invoice.setCustomer(customer);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setDate(date);
        invoice.setTotal(requestDto.getTotal());
        invoice.setTax(requestDto.getTax());
        invoice.setQuantity(requestDto.getQuantity());
        invoice.setInvoiceStatus(invoice.getInvoiceStatus());
        invoice.setOrganization(org);
        invoice.setTemplate(template);
        invoice.setHeader(requestDto.getHeader());
        invoice.setCustomerDetails(requestDto.getCustomerDetails());
        invoice.setInvoiceMeta(requestDto.getInvoiceMeta());
        invoice.setItems(requestDto.getItems());
        invoice.setSummary(requestDto.getSummary());
        invoice.setFooter(requestDto.getFooter());
        invoiceRepository.save(invoice);
    }

    @Transactional
    public void duplicate(Long invoiceId) {
        Invoice existingInvoice = invoiceRepository.findByInvoiceId_AndOrganization_OrgId(invoiceId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Invoice not found."));

        Invoice newInvoice = new Invoice();
        newInvoice.setCustomer(existingInvoice.getCustomer());
        newInvoice.setInvoiceNumber(existingInvoice.getInvoiceNumber() + " - copy");
        newInvoice.setDate(existingInvoice.getDate());
        newInvoice.setTotal(existingInvoice.getTotal());
        newInvoice.setTax(existingInvoice.getTax());
        newInvoice.setQuantity(existingInvoice.getQuantity());
        newInvoice.setInvoiceStatus(InvoiceStatus.DRAFT);
        newInvoice.setOrganization(existingInvoice.getOrganization());
        newInvoice.setTemplate(existingInvoice.getTemplate());
        newInvoice.setHeader(existingInvoice.getHeader());
        newInvoice.setCustomerDetails(existingInvoice.getCustomerDetails());
        newInvoice.setInvoiceMeta(existingInvoice.getInvoiceMeta());
        newInvoice.setItems(existingInvoice.getItems());
        newInvoice.setSummary(existingInvoice.getSummary());
        newInvoice.setFooter(existingInvoice.getFooter());

        invoiceRepository.save(newInvoice);
    }

    @Transactional
    public void deleteInvoice(Map<String, List<Long>> idsMap) {
        List<Long> idsList = idsMap.get("invoice_ids");
        if (invoiceRepository.countByInvoiceIdInAndOrganization_OrgId(idsList, OrgContext.getOrgId()) != idsList.size()) {
            throw new NotFountException("Selected invoices not found.");
        }

        invoiceRepository.deleteByInvoiceIdInAndOrganization_OrgId(idsList, OrgContext.getOrgId());
    }
}
