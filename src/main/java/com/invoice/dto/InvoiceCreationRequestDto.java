package com.invoice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class InvoiceCreationRequestDto {
    private Long invoiceId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long templateId;

    @NotNull
    private Double total;

    @NotNull
    private Double tax;

    @NotNull
    private Double quantity;

    @NotNull
    private Map<String, Object> header;

    @NotNull
    private Map<String, Object> invoiceMeta;

    @NotNull
    private Map<String, Object> customerDetails;

    @NotNull
    private Map<String, Object> items;

    @NotNull
    private Map<String, Object> summary;

    @NotNull
    private Map<String, Object> footer;
}
