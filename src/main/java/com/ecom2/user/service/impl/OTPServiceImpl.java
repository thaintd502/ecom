package com.ecom2.user.service.impl;

import com.ecom2.user.entity.OTP;
import com.ecom2.user.entity.User;
import com.ecom2.user.repository.OTPRepository;
import com.ecom2.user.service.OTPService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OTPRepository otpRepository;

    public void saveOTP(OTP otp) {
        otpRepository.save(otp);
    }

    @Override
    public Optional<OTP> findByUserAndOtpCode(User user, String otpCode) {
        return otpRepository.findByUserAndOtpCode(user, otpCode);
    }

    @Transactional
    public void deleteByUser(User user) {
        otpRepository.deleteByUser(user);
    }

    @Override
    public Optional<OTP> findByUser(User user) {
        return otpRepository.findByUser(user);
    }

    @Override
    public Optional<OTP> findByUserAndVerified(User user, boolean verified){
        return otpRepository.findByUserAndVerified(user, verified);
    }

}
