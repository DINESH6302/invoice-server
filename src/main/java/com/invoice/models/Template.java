package com.invoice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "templates")
public class Template extends BaseModel {

    @Id
    @Column(name = "template_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "font_family", nullable = false)
    private String fontFamily;

    @Column(name = "font_size", nullable = false)
    private Integer fontSize;

    @Column(name = "accent_color", nullable = false)
    private Integer accentColor;

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
}
