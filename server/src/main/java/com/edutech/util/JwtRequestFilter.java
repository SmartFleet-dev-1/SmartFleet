package com.edutech.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.edutech.entity.User;
import com.edutech.service.UserService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Debug  - Extracted username: " + username);
            } catch (Exception e) {
                // invalid token
                System.out.println("Debug - Token error: " + e.getMessage());
            }
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userService.findByUsername(username);
            System.out.println("Debug  -Found user: " + (user != null));
            if (user == null) {
                chain.doFilter(request, response);
                return;
            }

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), authorities);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("Debug  - validateToken FAILED");
                System.out.println("JWT username: " + jwtUtil.extractUsername(jwt));
                System.out.println("UserDetails username: " + userDetails.getUsername());
            }
        }
        chain.doFilter(request, response);
    }
}
