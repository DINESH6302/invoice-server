package com.invoice.service;

import com.invoice.context.OrgContext;
import com.invoice.dto.InvoiceCreationRequestDto;
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

    public void getInvoices() {
        // Implementation for fetching invoices will go here
    }

    @Transactional
    public void createInvoice(InvoiceCreationRequestDto requestDto) {
        Long customerId = requestDto.getCustomerId();
        Customer customer = customerRepository.findByCustomerIdAndOrganization_OrgId(customerId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Customer not found."));

        Template template = templateRepository.findByTemplateIdAndOrganization_OrgId(requestDto.getTemplateId(), OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("You don't have access to this template."));

        Organization org = orgRepository.findById(OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Organization not found."));

        Map<String, Object> header = requestDto.getHeader();
        String invoiceNumber = (String) header.get("Invoice No");
        LocalDate date = LocalDate.parse((String) header.get("Date"));

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setDate(date);
        invoice.setInvoiceStatus(InvoiceStatus.DRAFT);
        invoice.setOrganization(org);
        invoice.setTemplate(template);


    }
}
