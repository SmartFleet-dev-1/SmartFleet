package com.edutech.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutech.entity.*;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.DriverRepository;
import com.edutech.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DriverRepository driverRepository;

    // =============================================
    // ✅ EXISTING METHODS — UNTOUCHED
    // =============================================

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // =============================================
    // ✅ MODIFIED — registerUser
    // =============================================

    public User registerUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == Role.ADMIN) {
            List<User> activeAdmins = userRepository.findAllActiveAdminsOrderById();
            if (activeAdmins.isEmpty()) {
                user.setStatus(UserStatus.ACTIVE);
            } else {
                user.setStatus(UserStatus.PENDING);
            }
        } else if (user.getRole() == Role.DRIVER) {
            user.setStatus(UserStatus.ACTIVE);
        }

        User saved = userRepository.save(user);

        if (saved.getRole() == Role.DRIVER && saved.getStatus() == UserStatus.ACTIVE) {
            createDriverFromUser(saved);
        }

        return saved;
    }

    // =============================================
    // ✅ NEW PUBLIC METHODS
    // =============================================

    public boolean isSuperAdmin(User user) {
        List<User> activeAdmins = userRepository.findAllActiveAdminsOrderById();
        if (activeAdmins.isEmpty()) return false;
        return activeAdmins.get(0).getId().equals(user.getId());
    }

    public User getSuperAdmin() {
        List<User> activeAdmins = userRepository.findAllActiveAdminsOrderById();
        if (activeAdmins.isEmpty()) {
            throw new ResourceNotFoundException("No super admin found");
        }
        return activeAdmins.get(0);
    }

    public List<User> getPendingAdmins() {
        return userRepository.findByStatusAndRole(UserStatus.PENDING, Role.ADMIN);
    }

    public List<User> getPendingUsers() {
        return userRepository.findByStatus(UserStatus.PENDING);
    }

    public User approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStatus(UserStatus.ACTIVE);
        User saved = userRepository.save(user);

        if (saved.getRole() == Role.DRIVER) {
            createDriverFromUser(saved);
        }

        return saved;
    }

    public User rejectUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStatus(UserStatus.REJECTED);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    public User updateProfile(User updated, String currentPassword, String loggedInUsername) {
        User existing = findByUsername(loggedInUsername);

        if (!passwordEncoder.matches(currentPassword, existing.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!existing.getUsername().equals(updated.getUsername())) {
            if (existsByUsername(updated.getUsername())) {
                throw new RuntimeException("Username already exists, try another");
            }
            existing.setUsername(updated.getUsername());
        }

        existing.setName(updated.getName());
        existing.setContactNumber(updated.getContactNumber());

        if (existing.getRole() == Role.DRIVER) {
            existing.setLicenseNumber(updated.getLicenseNumber());
            existing.setExperienceYears(updated.getExperienceYears());
            existing.setAddress(updated.getAddress());
            existing.setLicenseExpiryDate(updated.getLicenseExpiryDate());

            updateDriverFromUser(existing);
        }

        return userRepository.save(existing);
    }

    public void changePassword(String oldPassword, String newPassword, String loggedInUsername) {
        User user = findByUsername(loggedInUsername);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public long countPending() {
        return userRepository.countByStatus(UserStatus.PENDING);
    }

    public List<User> getActiveByRole(Role role) {
        return userRepository.findByStatusAndRole(UserStatus.ACTIVE, role);
    }

    // =============================================
    // ✅ PRIVATE HELPERS
    // =============================================

    private void createDriverFromUser(User user) {
        Driver driver = new Driver();
        driver.setDriverName(user.getName());
        driver.setLicenseNumber(user.getLicenseNumber() != null ? user.getLicenseNumber() : "PENDING");
        driver.setPhoneNumber(user.getContactNumber() != null ? String.valueOf(user.getContactNumber()) : "0000000000");
        driver.setExperienceYears(user.getExperienceYears() != null ? user.getExperienceYears() : 0);
        driver.setAddress(user.getAddress() != null ? user.getAddress() : "Not provided");
        driver.setAvailabilityStatus("Active");
        driver.setUser(user);

        if (user.getLicenseExpiryDate() != null && !user.getLicenseExpiryDate().isEmpty()) {
            driver.setLicenseExpiryDate(LocalDate.parse(user.getLicenseExpiryDate()));
        }

        driverRepository.save(driver);
    }

    private void updateDriverFromUser(User user) {
        List<Driver> allDrivers = driverRepository.findAll();
        for (Driver d : allDrivers) {
            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                d.setDriverName(user.getName());
                d.setPhoneNumber(String.valueOf(user.getContactNumber()));
                d.setLicenseNumber(user.getLicenseNumber());
                d.setExperienceYears(user.getExperienceYears() != null ? user.getExperienceYears() : 0);
                d.setAddress(user.getAddress());
                if (user.getLicenseExpiryDate() != null && !user.getLicenseExpiryDate().isEmpty()) {
                    d.setLicenseExpiryDate(LocalDate.parse(user.getLicenseExpiryDate()));
                }
                driverRepository.save(d);
                break;
            }
        }
    }
}