package com.invoice.controller;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.CustomerCreationReqDto;
import com.invoice.dto.CustomerDetailsResponseDto;
import com.invoice.dto.CustomerSummaryResponseDto;
import com.invoice.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List>> getAllCustomers() {
        List<CustomerDetailsResponseDto> customerDetailsDto = customerService.getAllCustomers();

        ApiResponse<List> apiResponse = new ApiResponse<>(
                true,
                "Customers details fetched successfully.",
                customerDetailsDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<CustomerSummaryResponseDto>>> getCustomersSummary() {
        List<CustomerSummaryResponseDto> customersNameList = customerService.getCustomersSummary();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true,
                "Customers summary fetched successfully.",
                customersNameList
        ));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDetailsResponseDto>> getCustomer(@PathVariable Long customerId) {
        CustomerDetailsResponseDto customer = customerService.getCustomerById(customerId);


        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true,
                "Customer details fetched successfully.",
                customer
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map>> createCustomer(@RequestBody @Valid CustomerCreationReqDto requestDto) {
        Long customerId = customerService.createCustomer(requestDto);

        ApiResponse<Map> apiResponse = new ApiResponse<>(
                true,
                "Customer successfully created.",
                Map.of("customerId", String.valueOf(customerId))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> updateCustomer(@PathVariable Long customerId, @RequestBody @Valid CustomerCreationReqDto requestDto) {
        customerService.updateCustomer(customerId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true, "Customer updated successfully.", null));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ApiResponse<>(true, "Customer deleted successfully.", null)
        );
    }
}
