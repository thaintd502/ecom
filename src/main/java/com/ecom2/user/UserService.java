package com.ecom2.user;

import com.ecom2.auth.payload.request.SignupRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUserName(String username);
    User saveOrUpdate(User user);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);
    User signUp(SignupRequest request);

    List<User> getAllUsers();
}