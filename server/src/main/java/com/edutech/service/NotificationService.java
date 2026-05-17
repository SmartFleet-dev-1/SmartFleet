package com.edutech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.dto.NotificationDTO;
import com.edutech.entity.Notification;
import com.edutech.entity.Role;
import com.edutech.entity.User;
import com.edutech.entity.UserStatus;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.NotificationRepository;
import com.edutech.repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // Entity → DTO
    private NotificationDTO mapToDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(n.getNotificationId());
        dto.setMessage(n.getMessage());
        dto.setType(n.getType());
        dto.setRead(n.isRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }

    // GET all notifications for a user
    public List<NotificationDTO> getNotifications(Long userId) {
        return notificationRepository.findByRecipientId(userId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // GET unread notifications
    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findUnreadByRecipientId(userId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // COUNT unread
    public long countUnread(Long userId) {
        return notificationRepository.countUnreadByRecipientId(userId);
    }

    // MARK as read
    public NotificationDTO markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        n.setRead(true);
        return mapToDTO(notificationRepository.save(n));
    }

    // MARK all as read
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findUnreadByRecipientId(userId);
        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }

    // =============================================
    // AUTO-GENERATE NOTIFICATIONS
    // =============================================

    // Create notification for a specific user
    public void createNotification(String message, String type, User recipient) {
        Notification n = new Notification(message, type, recipient);
        notificationRepository.save(n);
    }

    // Notify all active admins
    public void notifyAllAdmins(String message, String type) {
        List<User> admins = userRepository.findByStatusAndRole(UserStatus.ACTIVE, Role.ADMIN);
        for (User admin : admins) {
            createNotification(message, type, admin);
        }
    }

    // Notify super admin only
    public void notifySuperAdmin(String message, String type) {
        List<User> activeAdmins = userRepository.findAllActiveAdminsOrderById();
        if (!activeAdmins.isEmpty()) {
            createNotification(message, type, activeAdmins.get(0));
        }
    }

    // --- Specific notification generators ---

    public void notifyNewAdminRegistration(String adminUsername) {
        notifySuperAdmin(
                "New admin registration: " + adminUsername + " is pending approval",
                "APPROVAL"
        );
    }

    public void notifyUserApproved(User user) {
        createNotification(
                "Your account has been approved. Welcome to Move IQ!",
                "APPROVAL",
                user
        );
    }

    public void notifyUserRejected(User user) {
        createNotification(
                "Your admin registration has been rejected.",
                "APPROVAL",
                user
        );
    }

    public void notifyTripAssigned(User driverUser, String startLocation, String endLocation) {
        createNotification(
                "New trip assigned: " + startLocation + " → " + endLocation,
                "TRIP_ASSIGNED",
                driverUser
        );
    }

    public void notifyTripCompleted(String driverName, String startLocation, String endLocation) {
        notifyAllAdmins(
                "Trip completed by " + driverName + ": " + startLocation + " → " + endLocation,
                "TRIP_COMPLETED"
        );
    }

    public void notifyDriverAssignedToVehicle(User driverUser, String vehicleNumber) {
        createNotification(
                "You have been assigned to vehicle: " + vehicleNumber,
                "VEHICLE_ASSIGNED",
                driverUser
        );
    }

    public void notifyDriverUnassignedFromVehicle(User driverUser, String vehicleNumber) {
        createNotification(
                "You have been unassigned from vehicle: " + vehicleNumber,
                "VEHICLE_UNASSIGNED",
                driverUser
        );
    }
}