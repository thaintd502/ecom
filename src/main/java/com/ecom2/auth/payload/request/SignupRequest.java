package com.ecom2.auth.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String name;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date created;
    private Set<String> listRoles;
}
