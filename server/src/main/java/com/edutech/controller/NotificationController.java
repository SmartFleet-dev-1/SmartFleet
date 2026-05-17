package com.edutech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.NotificationDTO;
import com.edutech.entity.User;
import com.edutech.service.NotificationService;
import com.edutech.service.UserService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    // GET /api/notifications
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.getNotifications(user.getId()));
    }

    // GET /api/notifications/unread
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.getUnreadNotifications(user.getId()));
    }

    // GET /api/notifications/unread-count
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.countUnread(user.getId()));
    }

    // PUT /api/notifications/{id}/read
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // PUT /api/notifications/read-all
    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok("All notifications marked as read");
    }
}