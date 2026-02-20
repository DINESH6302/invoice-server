package com.invoice.controller;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.InvoiceCreationRequestDto;
import com.invoice.dto.InvoiceResponseDto;
import com.invoice.dto.InvoiceSummaryDto;
import com.invoice.dto.StatusUpdateDto;
import com.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/invoices")
@CrossOrigin(origins = "http://localhost:3000")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceSummaryDto>>> getInvoicesSummary() {
        List<InvoiceSummaryDto> invoices = invoiceService.getInvoicesSummary();

        ApiResponse<List<InvoiceSummaryDto>> response = ApiResponse.<List<InvoiceSummaryDto>>builder()
                .success(true)
                .message("Invoices fetched successfully.")
                .data(invoices)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> getInvoice(@PathVariable Long invoiceId) {
        InvoiceResponseDto response = invoiceService.getInvoice(invoiceId);

        ApiResponse<InvoiceResponseDto> apiResponse = ApiResponse.<InvoiceResponseDto>builder()
                .success(true)
                .message("Invoice fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map>> createInvoice(@RequestBody InvoiceCreationRequestDto requestDto) {
        Long invoiceId = invoiceService.createInvoice(requestDto);

        ApiResponse<Map> response = ApiResponse.<Map>builder()
                .success(true)
                .message("Draft Saved Successfully.")
                .data(Map.of("invoiceId", invoiceId))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Void>> generateInvoices(@RequestBody InvoiceCreationRequestDto requestDto) {
        invoiceService.createInvoice(requestDto);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Invoice Saved Successfully.")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<Map>> updateInvoice(@PathVariable Long invoiceId, @RequestBody InvoiceCreationRequestDto requestDto) {
        invoiceService.updateInvoice(invoiceId, requestDto);
        ApiResponse<Map> response = ApiResponse.<Map>builder()
                .success(true)
                .message("Invoice updated successfully.")
                .data(Map.of("invoiceId", invoiceId))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{invoiceId}/duplicate")
    public ResponseEntity<ApiResponse<Void>> duplicateInvoice(@PathVariable Long invoiceId) {
        invoiceService.duplicate(invoiceId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Duplicate invoice created.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInvoice(@RequestBody Map<String, List<Long>> idsList) {
        invoiceService.deleteInvoice(idsList);

        return ResponseEntity.noContent().build();
    }
}
