package com.invoice.dto;

import com.invoice.models.Enum.InvoiceStatus;

import java.time.LocalDate;

public interface InvoiceSummary {
    Long getInvoiceId();

    Long getTemplateId();

    String getInvoiceNumber();

    String getCustomerName();

    LocalDate getInvoiceDate();

    Double getTotal();

    InvoiceStatus getInvoiceStatus();
}
