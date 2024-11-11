package com.ecom2.customer.dto;

import com.ecom2.role.Role;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private String userName;
    private String password;
    private String email;

    private String name;
    private String phone;
    private String gender;
    private String birthdate;
    private MultipartFile imageUrl;

    private String address;
    private String city;
    private String district;
    private String commune;

}
