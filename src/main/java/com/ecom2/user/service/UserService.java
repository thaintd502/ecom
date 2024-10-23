package com.ecom2.user.service;

import com.ecom2.auth.payload.request.SignupRequest;
import com.ecom2.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUserName(String username);
    User saveOrUpdate(User user);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);
    User signUp(SignupRequest request);

    List<User> getAllUsers();

    void deleteUserById(int id);

    User findById(int id);

    Optional<User> findByEmail(String email);
    void sendOtpEmail(String to, String otp);
    boolean changePassword(String username, String oldPassword, String newPassword);

}