package com.edutech.dto;

import com.edutech.entity.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

     // Add the required code here!
     private String token;
     private String role;
     private String username;

     public LoginResponse() {
     }

     public LoginResponse(String token, String role, String username) {
          this.token = token;
          this.role = role;
          this.username = username;
     }

     public String getToken() {
          return token;
     }

     public void setToken(String t) {
          this.token = t;
     }

     public String getRole() {
          return role;
     }

     public void setRole(String r) {
          this.role = r;
     }

     public String getUsername() {
          return username;
     }

     public void setUsername(String u) {
          this.username = u;
     }
}
