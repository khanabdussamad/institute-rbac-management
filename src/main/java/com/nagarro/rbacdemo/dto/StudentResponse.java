package com.nagarro.rbacdemo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentResponse {

    private UUID id;
    private UUID userId;
    private UUID departmentId;
    private String rollNumber;
    private LocalDateTime createdAt;

    public StudentResponse() {
    }

    public StudentResponse(UUID id, UUID userId, UUID departmentId, String rollNumber, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.departmentId = departmentId;
        this.rollNumber = rollNumber;
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

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
