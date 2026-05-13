package com.edutech.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutech.entity.User;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.UserRepository;

@Service
public class UserService {

     @Autowired
     private UserRepository userRepository;

     // ✅ Find user by username
     public User findByUsername(String username) {
          return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
     }

     // ✅ Check if username exists
     public boolean existsByUsername(String username) {
          return userRepository.existsByUsername(username);
     }

     // ✅ Save user
     public User saveUser(User user) {
          return userRepository.save(user);
     }
     

}
