package com.invoice.models;

import com.invoice.models.Enum.InvoiceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class Invoice extends BaseModel {

    @Id
    @Column(name = "invoice_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "tax_amount")
    private Integer taxAmount;

    @Column(columnDefinition = "jsonb")
    private String header;

    @Column(columnDefinition = "jsonb")
    private String meta;

    @Column(columnDefinition = "jsonb")
    private String ship;

    @Column(columnDefinition = "jsonb")
    private String items;

    @Column(columnDefinition = "jsonb")
    private String total;

    @Column(columnDefinition = "jsonb")
    private String footer;

    @Column(name = "invoice_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;
}
