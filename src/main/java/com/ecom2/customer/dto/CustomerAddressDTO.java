package com.ecom2.customer.dto;

import lombok.Data;

@Data
public class CustomerAddressDTO {
    private int addressId;
    private String address;
    private String commune;
    private String district;
    private String city;
    private String country;
}
