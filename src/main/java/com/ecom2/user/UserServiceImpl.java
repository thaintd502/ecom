package com.ecom2.user;

import com.ecom2.auth.payload.request.SignupRequest;
import com.ecom2.role.ERole;
import com.ecom2.role.Role;
import com.ecom2.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Override
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
    @Override
    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean existsByUserName(String username) {
        return userRepository.existsUserByUserName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public User signUp(SignupRequest request) {
        User user = new User();
        if (request.getUserName() != null) user.setUserName(request.getUserName());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        user.setCreated(new Date());
        Set<String> strRole = request.getListRoles();
        Set<Role> listRoles = new HashSet<>();
        if (strRole == null) {
            Role userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: role is not found"));
            listRoles.add(userRole);
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: role is not found"));
                        listRoles.add(adminRole);
                    case "manager":
                        Role managerRole = roleService.findByRoleName(ERole.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("Error: role is not found"));
                        listRoles.add(managerRole);
                    case "user":
                        Role userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
