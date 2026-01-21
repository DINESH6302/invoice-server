package com.invoice.controllers;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.OrgCreationRequestDto;
import com.invoice.models.Organization;
import com.invoice.services.OrgService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/orgs")
@CrossOrigin(origins = "http://localhost:3000")
public class OrgController {

    private final OrgService orgService;

    @Autowired
    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllOrgNames() {
        List<String> orgNames = orgService.getAllOrgNames();

        return ResponseEntity.ok().body(orgNames);
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrgByUser() {
        List<Organization> orgList = orgService.getAllOrgByUSer();

        return ResponseEntity.ok().body(orgList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map>> createOrg(@RequestBody @Valid OrgCreationRequestDto requestDto) {
        Organization org = orgService.createOrg(requestDto);

        ApiResponse<Map> response = new ApiResponse<>();
        Map<String, Object> data = Map.of("orgId", org.getOrgId());

        response.setSuccess(true);
        response.setData(data);
        response.setMessage("Organization created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<ApiResponse<Void>> updateTemplate(@PathVariable Long orgId, @RequestBody @Valid OrgCreationRequestDto requestDto) {
        orgService.updateOrg(orgId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Organization Updated successfully.", null));
    }

    @DeleteMapping("/{orgId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrg(@PathVariable Long orgId) {
        orgService.deleteOrg(orgId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Organization deleted successfully.", null));
    }
}
