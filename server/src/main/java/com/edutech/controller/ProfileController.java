package com.edutech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.entity.User;
import com.edutech.service.UserService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin("*")
public class ProfileController {

    @Autowired
    private UserService userService;

    // GET /api/profile
    @GetMapping
    public ResponseEntity<User> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    // PUT /api/profile?currentPassword=
    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestBody User updated,
            @RequestParam String currentPassword,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            User saved = userService.updateProfile(
                    updated, currentPassword, userDetails.getUsername());
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/profile/change-password?oldPassword=&newPassword=
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            userService.changePassword(
                    oldPassword, newPassword, userDetails.getUsername());
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}