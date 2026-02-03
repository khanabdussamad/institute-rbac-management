package com.nagarro.rbacdemo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EmployeeResponse {

    private UUID id;
    private UUID userId;
    private UUID departmentId;
    private String designation;
    private LocalDateTime createdAt;

    public EmployeeResponse() {
    }

    public EmployeeResponse(UUID id, UUID userId, UUID departmentId, String designation, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.departmentId = departmentId;
        this.designation = designation;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
