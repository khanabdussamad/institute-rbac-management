package com.nagarro.rbacdemo.dto;

import java.util.UUID;

public class DepartmentResponse {

    private UUID id;
    private String name;
    private UUID createdById;

    public DepartmentResponse() {
    }

    public DepartmentResponse(UUID id, String name, UUID createdById) {
        this.id = id;
        this.name = name;
        this.createdById = createdById;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCreatedById() {
        return createdById;
    }

    public void setCreatedById(UUID createdById) {
        this.createdById = createdById;
    }
}
