package com.invoice.service;

import com.invoice.context.OrgContext;
import com.invoice.dto.AddressDto;
import com.invoice.dto.OrgCreationRequestDto;
import com.invoice.dto.OrgDetailsResponseDto;
import com.invoice.exception.DuplicateResourceException;
import com.invoice.exception.NotFountException;
import com.invoice.models.Address;
import com.invoice.models.Organization;
import com.invoice.models.User;
import com.invoice.repositorie.OrgRepository;
import com.invoice.repositorie.UserRepository;
import com.invoice.repositorie.columnviews.OrgSummaryView;
import com.invoice.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<Map> getAllOrgNames() {

        List<OrgSummaryView> orgSummaryViews = orgRepo.findByUser_UserId(getUserObject().getUserId());

        List<Map> orgSummaryList = new ArrayList<>();

        for (OrgSummaryView v : orgSummaryViews) {
            Map<String, String> map = new HashMap<>();
            map.put("org_id", String.valueOf(v.getOrgId()));
            map.put("org_name", v.getOrgName());
            orgSummaryList.add(map);
        }

        return orgSummaryList;
    }

    public OrgDetailsResponseDto getOrgDetails() {

        Organization org = orgRepo.findById(OrgContext.getOrgId())
                .orElseThrow(() -> new NotFountException("Organization not found."));

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet(org.getAddress().getStreet());
        addressDto.setCity(org.getAddress().getCity());
        addressDto.setState(org.getAddress().getState());
        addressDto.setZipCode(org.getAddress().getZipCode());
        addressDto.setCountry(org.getAddress().getCountry());

        OrgDetailsResponseDto orgDetailsDto = new OrgDetailsResponseDto();
        orgDetailsDto.setOrgId(org.getOrgId());
        orgDetailsDto.setOrgName(org.getOrgName());
        orgDetailsDto.setGstNo(org.getGstNo());
        orgDetailsDto.setCurrency(org.getCurrency());
        orgDetailsDto.setAddress(addressDto);

        return orgDetailsDto;
    }

    @Transactional
    public Long createOrg(OrgCreationRequestDto requestDto) {
        Address address = new Address();
        address.setStreet(requestDto.getAddress().getStreet());
        address.setCity(requestDto.getAddress().getCity());
        address.setState(requestDto.getAddress().getState());
        address.setZipCode(requestDto.getAddress().getZipCode());
        address.setCountry(requestDto.getAddress().getCountry());

        Organization org = new Organization();
        org.setUser(getUserObject());
        org.setOrgName(requestDto.getOrgName());
        org.setGstNo(requestDto.getGstNo());
        org.setCurrency(requestDto.getCurrency());
        org.setAddress(address);

        // check if org name already exists
        if (orgRepo.existsByOrgName(org.getOrgName())) {
            throw new DuplicateResourceException("Organization with name " + org.getOrgName() + " already exists.");
        }
        orgRepo.save(org);

        return org.getOrgId();
    }

    @Transactional
    public void updateOrg(OrgCreationRequestDto requestDto) {
        Optional<Organization> org = orgRepo.findById(OrgContext.getOrgId());

        Address address = new Address();
        address.setStreet(requestDto.getAddress().getStreet());
        address.setCity(requestDto.getAddress().getCity());
        address.setState(requestDto.getAddress().getState());
        address.setCountry(requestDto.getAddress().getCountry());
        address.setZipCode(requestDto.getAddress().getZipCode());

        Organization existingOrg = org.get();
        existingOrg.setOrgName(requestDto.getOrgName());
        existingOrg.setGstNo(requestDto.getGstNo());
        existingOrg.setCurrency(requestDto.getCurrency());
        existingOrg.setAddress(address);

        orgRepo.save(existingOrg);
    }

    @Transactional
    public void deleteOrg() {
        orgRepo.deleteById(OrgContext.getOrgId());
    }

    public User getUserObject() {
        Long userId = UserUtil.getUserId();
        Optional<User> user = userRepo.findById(userId);

        if (user.isEmpty()) {
            throw new NotFountException("User not found.");
        }

        return user.get();
    }
}
