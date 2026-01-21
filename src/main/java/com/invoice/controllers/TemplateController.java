package com.invoice.controllers;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.TemplateCreationRequestDto;
import com.invoice.dto.TemplateSummaryDto;
import com.invoice.models.Template;
import com.invoice.repositories.TemplateSummaryView;
import com.invoice.services.TemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<TemplateSummaryView> templateSummaryList = templateService.getTemplatesSummary();
        List<TemplateSummaryDto> responseDto = new ArrayList<>();

        templateSummaryList.forEach(templateSummary -> {
                    responseDto.add(
                            new TemplateSummaryDto(templateSummary.getTemplateId(), templateSummary.getTemplateName(), templateSummary.getUpdatedAt())
                    );
                }
        );

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<Template> getTemplate(@PathVariable Long templateId) {

        Template template = templateService.getTemplate(templateId);
        return ResponseEntity.ok().body(template);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTemplate(@RequestBody @Valid TemplateCreationRequestDto creationReqDto) {

        Template template = templateService.createTemplate(creationReqDto);

        ApiResponse<Map> response = new ApiResponse<>();
        Map<String, Object> data = Map.of("templateId", template.getTemplateId());

        response.setSuccess(true);
        response.setData(data);
        response.setMessage("Template created successfully");

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<ApiResponse> updateTemplate(@PathVariable Long templateId, @Valid @RequestBody TemplateCreationRequestDto updateReqDto) {
        Template template = templateService.updateTemplate(templateId, updateReqDto);

        Map<String, Object> data = new HashMap<>();
        data.put("templateId", template.getTemplateId());
        data.put("templateName", template.getTemplateName());
        data.put("updatedAt", template.getUpdatedAt());

        ApiResponse<Map> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessage("Template Updated successfully.");

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);

        return ResponseEntity.noContent().build();
    }
}
