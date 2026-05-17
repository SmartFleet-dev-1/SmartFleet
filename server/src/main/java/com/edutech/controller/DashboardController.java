package com.edutech.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.DashboardStatsDTO;
import com.edutech.entity.User;
import com.edutech.service.DashboardService;
import com.edutech.service.UserService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    // GET /api/dashboard/admin
    @GetMapping("/admin")
    public ResponseEntity<DashboardStatsDTO> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    // GET /api/dashboard/driver
    @GetMapping("/driver")
    public ResponseEntity<Map<String, Object>> getDriverDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(dashboardService.getDriverDashboard(user));
    }
}