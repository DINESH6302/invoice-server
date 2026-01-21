package com.invoice.repositories;

import com.invoice.models.Organization;
import com.invoice.models.User;
import com.invoice.repositories.columnviews.OrgNameView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrgRepository extends JpaRepository<Organization, Long> {
    Boolean existsByOrgName(String orgName);

    List<Organization> getOrganizationByUser(User user);

    List<OrgNameView> findByUser_UserId(Long userId);
}
