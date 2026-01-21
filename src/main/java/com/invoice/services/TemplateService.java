package com.invoice.services;

import com.invoice.dto.TemplateCreationRequestDto;
import com.invoice.exception.DuplicateResourceException;
import com.invoice.exception.NotFountException;
import com.invoice.models.Template;
import com.invoice.repositories.TemplateRepository;
import com.invoice.repositories.columnviews.TemplateSummaryView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<TemplateSummaryView> getTemplatesSummary() {

        List<TemplateSummaryView> templateSummaryList = templateRepository.findAllBy();
        return templateSummaryList;
    }

    public Template getTemplate(Long templateId) {
        return templateRepository.findById(templateId).orElseThrow(() -> new NotFountException("Template not found."));
    }

    @Transactional
    public void createTemplate(TemplateCreationRequestDto creationReqDto) {
        try {
            // check if the template already exists with the name
            if (templateRepository.existsByTemplateName(creationReqDto.getTemplateName())) {
                throw new DuplicateResourceException("Template with name " + creationReqDto.getTemplateName() + " already exists");
            }

            Template template = mapDtoToTemplate(creationReqDto);

            templateRepository.save(template);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void updateTemplate(Long templateId, TemplateCreationRequestDto updateReqDto) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new NotFountException("Template not found."));

        mapDtoToTemplate(updateReqDto, template);

        templateRepository.save(template);
    }

    @Transactional
    public void deleteTemplate(Long templateId) {
        if (!templateRepository.existsByTemplateId(templateId)) {
            throw new NotFountException("Template not found.");
        }

        templateRepository.deleteById(templateId);
    }

    private Template mapDtoToTemplate(TemplateCreationRequestDto dto) {
        return mapDtoToTemplate(dto, new Template());
    }

    private Template mapDtoToTemplate(TemplateCreationRequestDto dto, Template template) {
        template.setTemplateName(dto.getTemplateName());
        template.setFontFamily(dto.getFontFamily());
        template.setFontSize(dto.getFontSize());
        template.setAccentColor(dto.getAccentColor());
        template.setHeader(dto.getHeader());
        template.setInvoiceMeta(dto.getInvoiceMeta());
        template.setCustomerDetails(dto.getCustomerDetails());
        template.setItems(dto.getItems());
        template.setTotal(dto.getTotal());
        template.setFooter(dto.getFooter());
        return template;
    }
}