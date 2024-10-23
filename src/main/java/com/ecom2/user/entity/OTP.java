package com.ecom2.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "otp")
@Data
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int otpId;

    private String otpCode;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private boolean verified=false;

    private Date expiryDate;
}

