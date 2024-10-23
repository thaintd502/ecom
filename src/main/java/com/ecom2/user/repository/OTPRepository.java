package com.ecom2.user.repository;

import com.ecom2.user.entity.OTP;
import com.ecom2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByUserAndOtpCode(User user, String otpCode);
    void deleteByUser(User user);
    Optional<OTP> findByUser(User user);
    Optional<OTP> findByUserAndVerified(User user, boolean verified);
}
