package com.invoice.controller;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.DefaultTemplateRequestDto;
import com.invoice.dto.TemplateCreationRequestDto;
import com.invoice.dto.TemplateDetailsDto;
import com.invoice.dto.TemplateSummaryDto;
import com.invoice.repositorie.columnviews.TemplateSummaryView;
import com.invoice.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/templates")
@CrossOrigin(origins = "http://localhost:3000")
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<List<TemplateSummaryDto>> getTemplatesSummary() {

        List<TemplateSummaryDto> templateSummaryList = templateService.getTemplatesSummary();

        return ResponseEntity.ok().body(templateSummaryList);
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<ApiResponse<TemplateDetailsDto>> getTemplate(@PathVariable Long templateId) {

        TemplateDetailsDto template = templateService.getTemplate(templateId);

        return ResponseEntity.ok().body(new ApiResponse<>(true, "Template fetched successfully.", template));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponse<TemplateDetailsDto>> getDefaultTemplate() {
        TemplateDetailsDto template = templateService.getDefaultTemplate();

        return ResponseEntity.ok().body(new ApiResponse<>(true, "Default Template fetched successfully.", template));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createTemplate(@RequestBody @Valid TemplateCreationRequestDto creationReqDto) {
        templateService.createTemplate(creationReqDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Template created successfully.", null));
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<ApiResponse<Void>> updateTemplate(@PathVariable Long templateId, @RequestBody @Valid TemplateCreationRequestDto updateReqDto) {
        templateService.updateTemplate(templateId, updateReqDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Template Updated successfully.", null));
    }

    @PatchMapping("/default")
    public ResponseEntity<ApiResponse<Void>> updateDefaultTemplate(@RequestBody DefaultTemplateRequestDto reqDto) {
        templateService.updateDefaultTemplate(reqDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Default template updated successfully.", null));
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Template deleted successfully.", null));
    }
}
