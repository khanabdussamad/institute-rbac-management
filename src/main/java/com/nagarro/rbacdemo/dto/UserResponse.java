package com.nagarro.rbacdemo.dto;

import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private String userType;
    private String status;

    public UserResponse() {
    }

    public UserResponse(UUID id, String username, String email, String userType, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
