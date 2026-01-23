package com.invoice.repositorie;

import com.invoice.models.Template;
import com.invoice.repositorie.columnviews.TemplateSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Boolean existsByTemplateName_AndOrganization_OrgId(String templateName, Long orgId);

    Boolean existsByTemplateId(Long templateId);

    List<TemplateSummaryView> findAllByOrganization_OrgId(Long orgId);

    Boolean existsByTemplateId_AndOrganization_OrgId(Long templateId, Long orgId);
    Optional<Template> findByTemplateId_AndOrganization_OrgId(Long templateId, Long orgId);

}