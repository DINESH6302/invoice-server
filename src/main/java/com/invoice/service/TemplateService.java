package com.invoice.service;

import com.invoice.context.OrgContext;
import com.invoice.dto.DefaultTemplateRequestDto;
import com.invoice.dto.TemplateCreationRequestDto;
import com.invoice.dto.TemplateDetailsDto;
import com.invoice.dto.TemplateSummaryDto;
import com.invoice.exception.DuplicateResourceException;
import com.invoice.exception.NotFountException;
import com.invoice.models.Organization;
import com.invoice.models.Template;
import com.invoice.repositorie.OrgRepository;
import com.invoice.repositorie.TemplateRepository;
import com.invoice.repositorie.columnviews.TemplateSummaryView;
import com.invoice.utils.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final OrgRepository orgRepository;

    public TemplateService(TemplateRepository templateRepository, OrgRepository orgRepository) {
        this.templateRepository = templateRepository;
        this.orgRepository = orgRepository;
    }

    public List<TemplateSummaryDto> getTemplatesSummary() {

        List<TemplateSummaryView> templateSummaryList = templateRepository.findAllByOrganization_OrgId(OrgContext.getOrgId());
        List<TemplateSummaryDto> responseDto = new ArrayList<>();
        templateSummaryList.forEach(templateSummary -> {
            responseDto.add(new TemplateSummaryDto(
                    templateSummary.getTemplateId(),
                    templateSummary.getIsDefault(),
                    templateSummary.getTemplateName(),
                    templateSummary.getUpdatedAt()));
        });

        return responseDto;
    }

    public TemplateDetailsDto getTemplate(Long templateId) {
        Template template = templateRepository.findByTemplateIdAndOrganization_OrgId(templateId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Template not found."));

        TemplateDetailsDto templateDetailsDto = new TemplateDetailsDto();
        templateDetailsDto.setTemplateId(template.getTemplateId());
        templateDetailsDto.setTemplateName(template.getTemplateName());
        templateDetailsDto.setIsDefault(template.getIsDefault());
        templateDetailsDto.setFontFamily(template.getFontFamily());
        templateDetailsDto.setFontSize(template.getFontSize());
        templateDetailsDto.setAccentColor(template.getAccentColor());
        templateDetailsDto.setHeader(template.getHeader());
        templateDetailsDto.setInvoiceMeta(template.getInvoiceMeta());
        templateDetailsDto.setCustomerDetails(template.getCustomerDetails());
        templateDetailsDto.setItems(template.getItems());
        templateDetailsDto.setTotal(template.getTotal());
        templateDetailsDto.setFooter(template.getFooter());

        return templateDetailsDto;
    }

    public TemplateDetailsDto getDefaultTemplate() {
        Template template = templateRepository.findByOrganization_OrgIdAndIsDefaultTrue(OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("No default template not found, make any template as default."));

        TemplateDetailsDto templateDetailsDto = new TemplateDetailsDto();
        templateDetailsDto.setTemplateId(template.getTemplateId());
        templateDetailsDto.setTemplateName(template.getTemplateName());
        templateDetailsDto.setIsDefault(template.getIsDefault());
        templateDetailsDto.setFontFamily(template.getFontFamily());
        templateDetailsDto.setFontSize(template.getFontSize());
        templateDetailsDto.setAccentColor(template.getAccentColor());
        templateDetailsDto.setHeader(template.getHeader());
        templateDetailsDto.setInvoiceMeta(template.getInvoiceMeta());
        templateDetailsDto.setCustomerDetails(template.getCustomerDetails());
        templateDetailsDto.setItems(template.getItems());
        templateDetailsDto.setTotal(template.getTotal());
        templateDetailsDto.setFooter(template.getFooter());

        return templateDetailsDto;
    }

    @Transactional
    public void createTemplate(TemplateCreationRequestDto creationReqDto) {
        try {
            Long orgId = OrgContext.getOrgId();

            // check if the template already exists with the name
            if (templateRepository.existsByTemplateName_AndOrganization_OrgId(creationReqDto.getTemplateName(), orgId)) {
                throw new DuplicateResourceException("Template with name " + creationReqDto.getTemplateName() + " already exists");
            }

            Organization orgObj = orgRepository.findById(orgId).orElseThrow(() -> new NotFountException("Organization not found."));

            Template template = mapDtoToTemplate(creationReqDto);
            template.setOrganization(orgObj);

            templateRepository.save(template);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void updateTemplate(Long templateId, TemplateCreationRequestDto updateReqDto) {
        Template template = templateRepository.findByTemplateIdAndOrganization_OrgId(templateId, OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Template not found."));

        mapDtoToTemplate(updateReqDto, template);

        templateRepository.save(template);
    }

    public void updateDefaultTemplate(DefaultTemplateRequestDto reqDto) {
        // disable existing default template
        templateRepository.disableDefaultTemplate(OrgContext.getOrgId());

        // set new default template
        int updatedRows = templateRepository.updateDefaultTemplate(reqDto.getTemplateId(), reqDto.getIsDefault(), OrgContext.getOrgId());
        if (updatedRows == 0) {
            throw new NotFountException("Template not found.");
        }
    }

    @Transactional
    public void deleteTemplate(Long templateId) {

        if (templateRepository.existsByTemplateIdAndIsDefault(templateId, true)) {
            throw new IllegalStateException("Cannot delete the default template. Please set another template as default first.");
        }

        if (!templateRepository.existsByTemplateId_AndOrganization_OrgId(templateId, OrgContext.getOrgId())) {
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