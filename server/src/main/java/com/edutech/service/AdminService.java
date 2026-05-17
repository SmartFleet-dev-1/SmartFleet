package com.edutech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.entity.Role;
import com.edutech.entity.User;
import com.edutech.entity.UserStatus;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    // Get pending admin registrations
    public List<User> getPendingAdmins() {
        return userRepository.findByStatusAndRole(UserStatus.PENDING, Role.ADMIN);
    }

    // Approve admin (super admin only)
    public User approveAdmin(Long userId, User currentUser) {
        // Check if current user is super admin
        if (!userService.isSuperAdmin(currentUser)) {
            throw new RuntimeException("Only super admin can approve other admins");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("This user is not an admin");
        }

        user.setStatus(UserStatus.ACTIVE);
        User saved = userRepository.save(user);

        // Notify approved user
        notificationService.notifyUserApproved(saved);

        return saved;
    }

    // Reject admin (super admin only)
    public User rejectAdmin(Long userId, User currentUser) {
        if (!userService.isSuperAdmin(currentUser)) {
            throw new RuntimeException("Only super admin can reject other admins");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("This user is not an admin");
        }

        user.setStatus(UserStatus.REJECTED);
        User saved = userRepository.save(user);

        // Notify rejected user
        notificationService.notifyUserRejected(saved);

        return saved;
    }

    // Delete user (respects permission hierarchy)
    public void deleteUser(Long userId, User currentUser) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Super admin can't be deleted
        if (userService.isSuperAdmin(target)) {
            throw new RuntimeException("Super admin cannot be deleted");
        }

        // Regular admin can only delete drivers
        if (!userService.isSuperAdmin(currentUser) && target.getRole() == Role.ADMIN) {
            throw new RuntimeException("Only super admin can delete other admins");
        }

        userRepository.delete(target);
    }

    // Get all active admins
    public List<User> getAllActiveAdmins() {
        return userRepository.findByStatusAndRole(UserStatus.ACTIVE, Role.ADMIN);
    }

    // Get all active drivers
    public List<User> getAllActiveDrivers() {
        return userRepository.findByStatusAndRole(UserStatus.ACTIVE, Role.DRIVER);
    }
}