package com.invoice.services;

import com.invoice.dto.OrgCreationRequestDto;
import com.invoice.exception.DuplicateResourceException;
import com.invoice.exception.NotFountException;
import com.invoice.models.Organization;
import com.invoice.models.User;
import com.invoice.repositories.OrgRepository;
import com.invoice.repositories.UserRepository;
import com.invoice.repositories.columnviews.OrgNameView;
import com.invoice.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class OrgService {

    private final OrgRepository orgRepo;
    private final UserRepository userRepo;

    @Autowired
    public OrgService(OrgRepository orgRepo, UserRepository userRepo) {
        this.orgRepo = orgRepo;
        this.userRepo = userRepo;
    }

    public List<String> getAllOrgNames() {

        List<OrgNameView> orgNameList = orgRepo.findByUser_UserId(getUserObject().getUserId());

        return orgNameList.stream()
                .map(OrgNameView::getOrgName)
                .toList();
    }

    public List<Organization> getAllOrgByUSer() {
        List<Organization> orgList = orgRepo.getOrganizationByUser(getUserObject());

        return orgList;
    }

    @Transactional
    public Organization createOrg(OrgCreationRequestDto requestDto) {

        Organization org = new Organization();
        org.setUser(getUserObject());
        org.setOrgName(requestDto.getOrgName());
        org.setGstNo(requestDto.getGstNo());
        org.setCurrency(requestDto.getCurrency());
        org.setAddress(requestDto.getAddress());

        // check if org name already exists
        if (orgRepo.existsByOrgName(org.getOrgName())) {
            throw new DuplicateResourceException("Organization with name " + org.getOrgName() + " already exists.");
        }

        return orgRepo.save(org);
    }

    public User getUserObject() {
        Long userId = UserUtil.getUserId();
        Optional<User> user = userRepo.findById(userId);

        if (user.isEmpty()) {
            throw new NotFountException("User not found.");
        }

        return user.get();
    }

    public void updateOrg(Long orgId, OrgCreationRequestDto requestDto) {
        Optional<Organization> org = orgRepo.findById(orgId);

        if (org.isEmpty()) {
            throw new NotFountException("Organization with id " + orgId + " not found.");
        }

        Organization existingOrg = org.get();
        existingOrg.setOrgName(requestDto.getOrgName());
        existingOrg.setGstNo(requestDto.getGstNo());
        existingOrg.setCurrency(requestDto.getCurrency());
        existingOrg.setAddress(requestDto.getAddress());

        orgRepo.save(existingOrg);
    }

    public void deleteOrg(Long orgId) {
        Optional<Organization> org = orgRepo.findById(orgId);

        if (org.isEmpty()) {
            throw new NotFountException("Organization with id " + orgId + " not found.");
        }

        orgRepo.deleteById(orgId);
    }
}
