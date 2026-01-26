package com.invoice.controller;

import com.invoice.dto.InvoiceCreationRequestDto;
import com.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
@CrossOrigin("http://localhost:3000")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public void getInvoices() {
        // Implementation for fetching invoices will go here
    }

    @PostMapping
    public void createInvoice(@RequestBody @Valid InvoiceCreationRequestDto requestDto) {

    }
}
