package com.nagarro.rbacdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class EmployeeRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID departmentId;

    @NotBlank
    @Size(max = 100)
    private String designation;

    public EmployeeRequest() {
    }

    public EmployeeRequest(UUID userId, UUID departmentId, String designation) {
        this.userId = userId;
        this.departmentId = departmentId;
        this.designation = designation;
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
}
