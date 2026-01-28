package com.invoice.repositorie;

import com.invoice.models.Customer;
import com.invoice.repositorie.columnviews.CustomerSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> getCustomerByOrganization_OrgId(Long orgId);

    List<CustomerSummaryView> findByOrganization_OrgId(Long organizationOrgId);

    Optional<Customer> findByCustomerIdAndOrganization_OrgId(Long customerId, Long orgId);
}
