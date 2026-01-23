package com.invoice.service;

import com.invoice.context.OrgContext;
import com.invoice.dto.AddressDto;
import com.invoice.dto.CustomerCreationReqDto;
import com.invoice.dto.CustomerDetailsResponseDto;
import com.invoice.exception.NotFountException;
import com.invoice.models.Address;
import com.invoice.models.Customer;
import com.invoice.models.Organization;
import com.invoice.repositorie.CustomerRepository;
import com.invoice.repositorie.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepo;
    private OrgRepository orgRepo;

    @Autowired
    public CustomerService(CustomerRepository customerRepo, OrgRepository orgRepo) {
        this.customerRepo = customerRepo;
        this.orgRepo = orgRepo;
    }

    public List<CustomerDetailsResponseDto> getAllCustomers() {
        Long orgId = OrgContext.getOrgId();

        List<Customer> customersList = customerRepo.getCustomerByOrganization_OrgId(orgId);

        List<CustomerDetailsResponseDto> responseDtoList = new ArrayList<>();

        for (Customer cx : customersList) {
            CustomerDetailsResponseDto dto = new CustomerDetailsResponseDto();
            dto.setCustomerId(cx.getCustomerId());
            dto.setCustomerName(cx.getCustomerName());
            dto.setGstNo(cx.getGstNo());

            AddressDto addressDto = new AddressDto();
            addressDto.setStreet(cx.getAddress().getStreet());
            addressDto.setCity(cx.getAddress().getCity());
            addressDto.setState(cx.getAddress().getState());
            addressDto.setCountry(cx.getAddress().getCountry());
            addressDto.setZipCode(cx.getAddress().getZipCode());
            dto.setAddress(addressDto);

            responseDtoList.add(dto);
        }

        return responseDtoList;
    }

    public CustomerDetailsResponseDto getCustomerById(Long customerId) {
        Optional<Customer> customerOpt = customerRepo.findById(customerId);

        if (customerOpt.isEmpty()) {
            throw new NotFountException("Customer not found.");
        }

        Customer cx = customerOpt.get();
        CustomerDetailsResponseDto dto = new CustomerDetailsResponseDto();
        dto.setCustomerId(cx.getCustomerId());
        dto.setCustomerName(cx.getCustomerName());
        dto.setGstNo(cx.getGstNo());

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet(cx.getAddress().getStreet());
        addressDto.setCity(cx.getAddress().getCity());
        addressDto.setState(cx.getAddress().getState());
        addressDto.setCountry(cx.getAddress().getCountry());
        addressDto.setZipCode(cx.getAddress().getZipCode());
        dto.setAddress(addressDto);

        return dto;
    }

    @Transactional
    public Long createCustomer(CustomerCreationReqDto requestDto) {
        Long orgId = OrgContext.getOrgId();
        Organization orgObj = orgRepo.findById(orgId).get();

        Address address = new Address();
        address.setStreet(requestDto.getAddress().getStreet());
        address.setCity(requestDto.getAddress().getCity());
        address.setState(requestDto.getAddress().getState());
        address.setCountry(requestDto.getAddress().getCountry());
        address.setZipCode(requestDto.getAddress().getZipCode());

        Customer customer = new Customer();
        customer.setCustomerName(requestDto.getCustomerName());
        customer.setGstNo(requestDto.getGstNo());
        customer.setOrganization(orgObj);
        customer.setAddress(address);

        customerRepo.save(customer);

        return customer.getCustomerId();
    }

    public void updateCustomer(Long customerId, CustomerCreationReqDto requestDto) {
        Optional<Customer> customerOpt = customerRepo.findById(customerId);

        if (customerOpt.isEmpty()) {
            throw new NotFountException("Customer not found.");
        }

        Customer customer = customerOpt.get();
        customer.setCustomerName(requestDto.getCustomerName());
        customer.setGstNo(requestDto.getGstNo());

        Address address = customer.getAddress();
        address.setStreet(requestDto.getAddress().getStreet());
        address.setCity(requestDto.getAddress().getCity());
        address.setState(requestDto.getAddress().getState());
        address.setCountry(requestDto.getAddress().getCountry());
        address.setZipCode(requestDto.getAddress().getZipCode());

        customer.setAddress(address);

        customerRepo.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        Optional<Customer> customerOpt = customerRepo.findById(customerId);

        if (customerOpt.isEmpty()) {
            throw new NotFountException("Customer not found.");
        }

        customerRepo.deleteById(customerId);
    }
}
