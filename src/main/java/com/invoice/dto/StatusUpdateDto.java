package com.invoice.dto;

import com.invoice.models.Enum.InvoiceStatus;
import lombok.Data;

import java.util.List;

@Data
public class StatusUpdateDto {
    List<Long> invoiceIds;
    InvoiceStatus status;
}
