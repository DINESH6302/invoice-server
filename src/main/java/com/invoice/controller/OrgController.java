package com.invoice.controller;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.OrgCreationRequestDto;
import com.invoice.dto.OrgDetailsResponseDto;
import com.invoice.service.OrgService;
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

    @GetMapping("/summary")
    public ResponseEntity<List<Map>> getAllOrgNames() {
        List<Map> orgNames = orgService.getAllOrgNames();

        return ResponseEntity.ok().body(orgNames);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OrgDetailsResponseDto>> getOrgDetails() {
        OrgDetailsResponseDto orgDetails = orgService.getOrgDetails();

        ApiResponse<OrgDetailsResponseDto> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(orgDetails);
        apiResponse.setMessage("Organization details fetched successfully.");

        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map>> createOrg(@RequestBody @Valid OrgCreationRequestDto requestDto) {
        Long orgId = orgService.createOrg(requestDto);

        ApiResponse<Map> response = new ApiResponse<>();
        Map<String, Object> data = Map.of("orgId", orgId);

        response.setSuccess(true);
        response.setData(data);
        response.setMessage("Organization created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateTemplate(@RequestBody @Valid OrgCreationRequestDto requestDto) {
        orgService.updateOrg(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Organization Updated successfully.", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteOrg() {
        orgService.deleteOrg();

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Organization deleted successfully.", null));
    }
}
