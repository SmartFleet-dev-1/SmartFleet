package com.edutech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.entity.User;
import com.edutech.service.AdminService;
import com.edutech.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    // GET /api/admin/pending-admins
    @GetMapping("/pending-admins")
    public ResponseEntity<List<User>> getPendingAdmins() {
        return ResponseEntity.ok(adminService.getPendingAdmins());
    }

    // PUT /api/admin/approve/{userId}
    @PutMapping("/approve/{userId}")
    public ResponseEntity<?> approveAdmin(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.findByUsername(userDetails.getUsername());
        try {
            User approved = adminService.approveAdmin(userId, currentUser);
            return ResponseEntity.ok(approved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/admin/reject/{userId}
    @PutMapping("/reject/{userId}")
    public ResponseEntity<?> rejectAdmin(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.findByUsername(userDetails.getUsername());
        try {
            User rejected = adminService.rejectAdmin(userId, currentUser);
            return ResponseEntity.ok(rejected);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/admin/delete/{userId}
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.findByUsername(userDetails.getUsername());
        try {
            adminService.deleteUser(userId, currentUser);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/admin/all-admins
    @GetMapping("/all-admins")
    public ResponseEntity<List<User>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllActiveAdmins());
    }

    // GET /api/admin/all-drivers
    @GetMapping("/all-drivers")
    public ResponseEntity<List<User>> getAllDrivers() {
        return ResponseEntity.ok(adminService.getAllActiveDrivers());
    }

    // GET /api/admin/is-super-admin
    @GetMapping("/is-super-admin")
    public ResponseEntity<Boolean> checkSuperAdmin(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(userService.isSuperAdmin(user));
    }
}