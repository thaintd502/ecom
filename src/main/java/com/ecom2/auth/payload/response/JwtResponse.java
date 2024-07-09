package com.ecom2.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String userName;
    private String email;
    private List<String> listRoles;

    public JwtResponse(String token, String userName, String email, List<String> listRoles) {
        this.token = token;
        this.userName = userName;
        this.email = email;
        this.listRoles = listRoles;
    }
}

