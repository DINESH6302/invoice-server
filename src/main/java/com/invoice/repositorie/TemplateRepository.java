package com.invoice.repositorie;

import com.invoice.models.Template;
import com.invoice.repositorie.columnviews.TemplateSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Boolean existsByTemplateName_AndOrganization_OrgId(String templateName, Long orgId);

    List<TemplateSummaryView> findAllByOrganization_OrgId(Long orgId);

    Boolean existsByTemplateId_AndOrganization_OrgId(Long templateId, Long orgId);

    Integer countByOrganization_OrgId(Long orgId);

    Boolean existsByTemplateIdAndIsDefault(Long templateId, Boolean isDefault);

    Optional<Template> findByTemplateIdAndOrganization_OrgId(Long templateId, Long orgId);

    Optional<Template> findByOrganization_OrgIdAndIsDefaultTrue(Long orgId);

    @Modifying
    @Transactional
    @Query("""
                update Template t
                set t.isDefault = false
                where t.organization.orgId = :orgId
                and  t.isDefault = true
            """)
    void disableDefaultTemplate(@Param("orgId") Long orgId);

    @Modifying
    @Transactional
    @Query("""
                update Template t
                set t.isDefault = :value
                where t.templateId = :id
                and t.organization.orgId = :orgId
            """)
    int updateDefaultTemplate(@Param("id") Long templateId, @Param("value") Boolean isDefault, @Param("orgId") Long orgId);

}