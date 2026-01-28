package com.invoice.dto;

import java.util.Map;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TemplateCreationRequestDto {

    @NotNull
    private String templateName;

    @NotNull
    private String fontFamily;

    @Min(14)
    @Max(30)
    private Integer fontSize;

    @NotNull
    private String accentColor;

    @NotNull
    private Map<String, Object> header;

    @NotNull
    private Map<String, Object> invoiceMeta;

    @NotNull
    private Map<String, Object> customerDetails;

    @NotNull
    private Map<String, Object> items;

    @NotNull
    private Map<String, Object> total;

    @NotNull
    private Map<String, Object> footer;
}