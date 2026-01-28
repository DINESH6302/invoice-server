package com.invoice.dto;

import com.invoice.models.Enum.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InvoiceSummaryDto {
    private Long invoiceId;
    private Long templateId;
    private String invoiceNumber;
    private String customerName;
    private LocalDate invoiceDate;
    private Double totalAmount;
    private InvoiceStatus invoiceStatus;
}
