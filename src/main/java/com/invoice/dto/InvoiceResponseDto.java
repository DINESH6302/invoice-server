package com.invoice.dto;

import com.invoice.models.Enum.InvoiceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class InvoiceResponseDto {
    private Long invoiceId;

    private Long customerId;

    private String invoiceNumber;

    private LocalDate date;

    private Double taxAmount;

    private Double totalAmount;

    private Map<String, Object> header;

    private Map<String, Object> invoiceMeta;

    private Map<String, Object> customerDetails;

    private Map<String, Object> items;

    private Map<String, Object> total;

    private Map<String, Object> footer;

    private InvoiceStatus status;
}
