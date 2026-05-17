package com.edutech.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.LoginRequest;
import com.edutech.dto.LoginResponse;
import com.edutech.entity.Role;
import com.edutech.entity.User;
import com.edutech.entity.UserStatus;
import com.edutech.service.NotificationService;
import com.edutech.service.UserService;
import com.edutech.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User registered = userService.registerUser(user);

            if (registered.getRole() == Role.ADMIN
                    && registered.getStatus() == UserStatus.PENDING) {
                notificationService.notifyNewAdminRegistration(registered.getUsername());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.findByUsername(request.getUsername());

            if (user.getStatus() == UserStatus.PENDING) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Your account is pending approval. Please wait for admin to approve.");
            }

            if (user.getStatus() == UserStatus.REJECTED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Your account has been rejected. Please contact admin.");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            String token = jwtUtil.generateToken(request.getUsername());

            return ResponseEntity.ok(
                    new LoginResponse(token, user.getRole().name(), user.getUsername(), user.getName()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
}