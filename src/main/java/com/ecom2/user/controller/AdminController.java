package com.ecom2.user.controller;

import com.ecom2.auth.jwt.JwtTokenProvider;
import com.ecom2.auth.payload.request.LoginRequest;
import com.ecom2.auth.payload.request.SignupRequest;
import com.ecom2.auth.payload.response.JwtResponse;
import com.ecom2.auth.payload.response.MessageResponse;
import com.ecom2.auth.security.CustomUserDetail;
import com.ecom2.role.ERole;
import com.ecom2.role.Role;
import com.ecom2.role.RoleService;
import com.ecom2.user.User;
import com.ecom2.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @PostMapping("/signin")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(customUserDetail.getUsername());
        List<String> listRoles = customUserDetail.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt, customUserDetail.getUsername(), customUserDetail.getEmail(), listRoles));
    }

    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());

    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
//        userService.deleteUserById(id);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable int id, @RequestBody SignupRequest request) {

        try {
            User user = userService.findById(id);

            if (request.getUserName() != null) user.setUserName(request.getUserName());
            if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
            if (request.getEmail() != null) user.setEmail(request.getEmail());

            Set<String> strRole = request.getListRoles();
            Set<Role> listRoles = new HashSet<>();
            if (strRole == null) {
                Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                listRoles.add(userRole);
            } else {
                strRole.forEach(role -> {
                    switch (role) {
                        case "ROLE_ADMIN":
                            Role adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                            listRoles.add(adminRole);
                            break; // Add break to avoid fall-through
                        case "ROLE_MANAGER":
                            Role managerRole = roleService.findByRoleName(ERole.ROLE_MANAGER)
                                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                            listRoles.add(managerRole);
                            break; // Add break to avoid fall-through
                        case "ROLE_USER":
                            Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                            listRoles.add(userRole);
                            break; // Add break to avoid fall-through
                    }
                });
            }
            user.setListRoles(listRoles);

            // Lưu thông tin người dùng đã được chỉnh sửa
            userService.saveOrUpdate(user);

            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if(userService.existsByUserName(request.getUserName())) return new ResponseEntity<>(new MessageResponse("Username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(request.getEmail())) return new ResponseEntity<>(new MessageResponse("Email already exists"), HttpStatus.BAD_REQUEST);
        User user = userService.signUp(request);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

}
