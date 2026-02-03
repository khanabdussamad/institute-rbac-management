package com.nagarro.rbacdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class StudentRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID departmentId;

    @NotBlank
    @Size(max = 100)
    private String rollNumber;

    public StudentRequest() {
    }

    public StudentRequest(UUID userId, UUID departmentId, String rollNumber) {
        this.userId = userId;
        this.departmentId = departmentId;
        this.rollNumber = rollNumber;
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
}
