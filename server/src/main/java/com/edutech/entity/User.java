package com.edutech.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User {

     // Add the required code here!
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @NotBlank
     @Column(unique = true)
     private String username;

     @NotBlank
     private String password;

     @NotBlank
     @Email
     @Column(unique = true)
     private String email;

     private Long contactNumber; // optional

     @NotNull
     @Enumerated(EnumType.STRING)
     private Role role;

     public User() {
     }

     public User(Long id, @NotBlank String username, @NotBlank String password, @NotBlank @Email String email,
               Long contactNumber, @NotNull Role role) {
          this.id = id;
          this.username = username;
          this.password = password;
          this.email = email;
          this.contactNumber = contactNumber;
          this.role = role;
     }

     public User(@NotBlank String username, @NotBlank String password, @NotBlank @Email String email,
               Long contactNumber,
               @NotNull Role role) {
          this.username = username;
          this.password = password;
          this.email = email;
          this.contactNumber = contactNumber;
          this.role = role;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getUsername() {
          return username;
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public String getPassword() {
          return password;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public Long getContactNumber() {
          return contactNumber;
     }

     public void setContactNumber(Long contactNumber) {
          this.contactNumber = contactNumber;
     }

     public Role getRole() {
          return role;
     }

     public void setRole(Role role) {
          this.role = role;
     }

     @Override
     public String toString() {
          return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
                    + ", contactNumber=" + contactNumber + ", role=" + role + "]";
     }

}
