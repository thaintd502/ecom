package com.ecom2.customer.dto;

import com.ecom2.role.Role;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
public class CustomerDTO {

    private String userName;
    private String password;
    private String email;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date created;
    private Set<Role> listRoles;

    private String name;
    private String phone;
    private String gender;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdate;
    private String imageUrl;

    // Address Information
    private String address;
    private String country;
    private String city;
    private String district;
    private String commune;

}
