package com.edutech.dto;

public class LoginResponse {

    private String token;
    private String role;
    private String username;
    private String name;

    public LoginResponse() {
    }

    public LoginResponse(String token, String role, String username, String name) {
        this.token = token;
        this.role = role;
        this.username = username;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }
}