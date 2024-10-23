
package com.ecom2.user;

import com.ecom2.auth.jwt.JwtTokenProvider;
import com.ecom2.auth.payload.request.LoginRequest;
import com.ecom2.auth.payload.request.SignupRequest;
import com.ecom2.auth.payload.response.JwtResponse;
import com.ecom2.auth.payload.response.MessageResponse;
import com.ecom2.auth.security.CustomUserDetail;
import com.ecom2.role.ERole;
import com.ecom2.role.Role;
import com.ecom2.role.RoleService;
import com.ecom2.user.entity.OTP;
import com.ecom2.user.entity.User;
import com.ecom2.user.service.OTPService;
import com.ecom2.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final OTPService otpService;

    @PostMapping("/api/v1/signin")
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

    @PostMapping("/api/v1/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if(userService.existsByUserName(request.getUserName())) return new ResponseEntity<>(new MessageResponse("Username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(request.getEmail())) return new ResponseEntity<>(new MessageResponse("Email already exists"), HttpStatus.BAD_REQUEST);
        User user = userService.signUp(request);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @GetMapping("/admin/get-user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/admin/edit-user/{id}")
    public ResponseEntity<?> editUser(@PathVariable int id, @RequestBody SignupRequest request) {

        try {

            User user = userService.findById(id);

            if (request.getUserName() != null) user.setUserName(request.getUserName().toLowerCase());
            if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
            if (request.getEmail() != null) user.setEmail(request.getEmail().toLowerCase());

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
                            break;
                        case "ROLE_MANAGER":
                            Role managerRole = roleService.findByRoleName(ERole.ROLE_MANAGER)
                                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                            listRoles.add(managerRole);
                            break;
                        case "ROLE_USER":
                            Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                            listRoles.add(userRole);
                            break;
                    }
                });
            }
            user.setListRoles(listRoles);

            userService.saveOrUpdate(user);

            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("api/v1/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }

        User user = userOptional.get();

        if(otpService.findByUser(user).isPresent()){
            otpService.deleteByUser(user);
        }

        String otpCode = generateOtp();
        OTP otp = new OTP();
        otp.setOtpCode(otpCode);
        otp.setUser(user);
        otp.setExpiryDate(generateExpiryDate());

        otpService.saveOTP(otp);

        userService.sendOtpEmail(user.getEmail(), otpCode);

        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(100000, 999999));
    }

    private Date generateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        return calendar.getTime();
    }

    @PostMapping("/api/v1/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otpCode) {
        // Kiểm tra xem email có tồn tại trong hệ thống không
        Optional<User> userOptional = userService.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }

        User user = userOptional.get();

        // Kiểm tra OTP có hợp lệ không
        Optional<OTP> otpOptional = otpService.findByUserAndOtpCode(user, otpCode);
        if (!otpOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }

        OTP otp = otpOptional.get();

        // Kiểm tra thời gian hết hạn của OTP
        if (otp.getExpiryDate().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired");
        }
        otp.setVerified(true);
        otpService.saveOTP(otp);
        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/api/v1/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        // Kiểm tra xem email có tồn tại trong hệ thống không
        Optional<User> userOptional = userService.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }

        User user = userOptional.get();

        // Kiểm tra OTP đã được xác thực hay chưa
        Optional<OTP> otpOptional = otpService.findByUserAndVerified(user, true);
        if (!otpOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP not verified or missing");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveOrUpdate(user);

        otpOptional.get().setVerified(false);
        otpService.saveOTP(otpOptional.get());

        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/api/v1/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        try {
            // Lấy token từ tiêu đề và loại bỏ tiền tố "Bearer "
            String jwtToken = token.replace("Bearer ", "");

            // Sử dụng hàm getUserNameFromJwt để lấy tên người dùng từ token
            String username = jwtTokenProvider.getUserNameFromJwt(jwtToken);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            // Xử lý đổi mật khẩu bằng cách gọi dịch vụ người dùng
            boolean isPasswordChanged = userService.changePassword(username, oldPassword, newPassword);

            if (isPasswordChanged) {
                return ResponseEntity.ok("Password changed successfully");
            } else {
                return ResponseEntity.badRequest().body("Old password is incorrect");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



}
