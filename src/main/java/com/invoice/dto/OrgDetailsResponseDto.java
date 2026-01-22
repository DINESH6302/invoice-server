package com.invoice.dto;

import lombok.Data;

@Data
public class OrgDetailsResponseDto {
    private Long orgId;
    private String orgName;
    private String gstNo;
    private String currency;
    private AddressDto address;
}
