package com.invoice.repositorie;

import com.invoice.models.Organization;
import com.invoice.repositorie.columnviews.OrgSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrgRepository extends JpaRepository<Organization, Long> {
    Boolean existsByOrgName(String orgName);

    List<OrgSummaryView> findByUser_UserId(Long userId);

    Boolean existsByUserIdAndOrgId(Long userId, Long orgId);
}
