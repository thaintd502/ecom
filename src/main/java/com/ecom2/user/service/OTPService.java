package com.ecom2.user.service;


import com.ecom2.user.entity.OTP;
import com.ecom2.user.entity.User;

import java.util.Optional;

public interface OTPService {
    void saveOTP(OTP otp);
    Optional<OTP> findByUserAndOtpCode(User user, String otpCode);
    void deleteByUser(User user);
    Optional<OTP> findByUser(User user);
    Optional<OTP> findByUserAndVerified(User user, boolean verified);
}
