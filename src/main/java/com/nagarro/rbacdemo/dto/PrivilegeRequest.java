// Create PrivilegeRequest DTO
package com.nagarro.rbacdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PrivilegeRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String resource;

    @NotBlank
    @Size(max = 50)
    private String action;

    public PrivilegeRequest() {
    }

    public PrivilegeRequest(String name, String resource, String action) {
        this.name = name;
        this.resource = resource;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
