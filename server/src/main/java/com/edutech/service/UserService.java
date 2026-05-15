package com.edutech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutech.entity.User;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Find user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> 
                    new ResourceNotFoundException("User not found"));
    }

    // Check if username exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Save user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Register new user with encoded password
    public User registerUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        // Encode password before saving
        user.setPassword(
            passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
