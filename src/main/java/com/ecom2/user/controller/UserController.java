
package com.ecom2.user.controller;

import com.ecom2.auth.jwt.JwtTokenProvider;
import com.ecom2.auth.payload.request.LoginRequest;
import com.ecom2.auth.payload.request.SignupRequest;
import com.ecom2.auth.payload.response.JwtResponse;
import com.ecom2.auth.payload.response.MessageResponse;
import com.ecom2.auth.security.CustomUserDetail;
import com.ecom2.user.User;
import com.ecom2.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
//    private final JavaMailSender mailSender;

    @PostMapping("/signin")
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if(userService.existsByUserName(request.getUserName())) return new ResponseEntity<>(new MessageResponse("Username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(request.getEmail())) return new ResponseEntity<>(new MessageResponse("Email already exists"), HttpStatus.BAD_REQUEST);
        User user = userService.signUp(request);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/viewInfor")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getInfor(@RequestHeader("Authorization") String token) {
        String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
        User user = userService.findByUserName(userName);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/updateInfor")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @RequestParam(required = false) String email, @RequestParam(required = false) String phoneNumber,
                                    @RequestParam(required = false) String address){
        String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
        User user = userService.findByUserName(userName);
        if(email != null) user.setEmail(email);
        if(phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if(address != null) user.setAddress(address);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("Update infor successfully"));
    }
    @PostMapping("/changepassword")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestParam String oldPass, @RequestParam String newPass){
        String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
        User user = userService.findByUserName(userName);
        if(passwordEncoder.matches(oldPass, user.getPassword())) {
            String bcreptNewPass = passwordEncoder.encode(newPass);
            user.setPassword(bcreptNewPass);
            userService.saveOrUpdate(user);
            return ResponseEntity.ok(new MessageResponse("change password successfully"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("oldPassword incorrect"));

    }
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> fogotPass(@RequestParam String username, @RequestParam String email) {
//        User user = userService.findByUserName(username);
//        if(user == null) return new ResponseEntity<>(new MessageResponse("Username not exists"),HttpStatus.NOT_FOUND);
//        if(!email.equals(user.getEmail())) return new ResponseEntity<>(new MessageResponse("Email not exists"), HttpStatus.NOT_FOUND);
//        Random random = new Random();
//        int otp = 100000 + random.nextInt(900000);
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(fromMail);
//        simpleMailMessage.setSubject("OTP");
//        simpleMailMessage.setText(String.valueOf(otp));
//        simpleMailMessage.setTo(email);
//        mailSender.send(simpleMailMessage);
//        user.setOtp(String.valueOf(otp));
//        userService.saveOrUpdate(user);
//        return new ResponseEntity<>(new MessageResponse("OTP sent to " + email), HttpStatus.OK);
//    }
    @GetMapping("/verify")
    public ResponseEntity<?> verifyOTP(@RequestParam String otp,@RequestParam String username){
        User user = userService.findByUserName(username);
        if(otp.equals(user.getOtp())){
            return ResponseEntity.ok(new MessageResponse("verified"));
        }
        return new ResponseEntity<>(new MessageResponse("Verification failed, please re-enter"), HttpStatus.NOT_FOUND);
    }
    @PutMapping("/setpassword")
    public ResponseEntity<?> setPassword(@RequestParam String newPass, @RequestParam String username){
        User user = userService.findByUserName(username);
        user.setPassword(passwordEncoder.encode(newPass));
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("Update password successfully"));
    }
}
