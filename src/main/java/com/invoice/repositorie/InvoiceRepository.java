package com.invoice.repositorie;

import com.invoice.dto.InvoiceSummary;
import com.invoice.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("""
            select i.invoiceId as invoiceId,
                   i.template.templateId as templateId,
                   i.invoiceNumber as invoiceNumber,
                   c.customerName as customerName,
                   i.date as invoiceDate,
                   i.totalAmount as totalAmount,
                   i.invoiceStatus as invoiceStatus
            from Invoice i join i.customer c
            where i.organization.orgId = :orgId
            """)
    List<InvoiceSummary> findByOrganization_OrgId(Long orgId);

    Boolean existsByInvoiceId_AndOrganization_OrgId(Long invoiceId, Long organizationOrgId);

    Optional<Invoice> findByInvoiceId_AndOrganization_OrgId(Long invoiceId, Long organizationOrgId);

    Integer countByInvoiceIdInAndOrganization_OrgId(List<Long> invoiceIds, Long orgId);

    List<Invoice> findByInvoiceIdInAndOrganization_OrgId(List<Long> invoiceIds, Long orgId);

    Integer deleteByInvoiceIdInAndOrganization_OrgId(List<Long> invoiceIds, Long orgId);
}
