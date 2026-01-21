package com.invoice.dto;

import com.invoice.models.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrgCreationRequestDto {

    @NotNull
    private String orgName;

    @NotNull
    private String gstNo;

    @NotNull
    private String currency;

    @NotNull
    private Address address;
}
