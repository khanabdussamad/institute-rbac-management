package com.nagarro.rbacdemo.dto;

import java.util.Set;
import java.util.UUID;

public class RoleResponse {
    private UUID id;
    private String name;
    private String description;
    private Set<UUID> privilegeIds;

    public RoleResponse() {
    }

    public RoleResponse(UUID id, String name, String description, Set<UUID> privilegeIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.privilegeIds = privilegeIds;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UUID> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(Set<UUID> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }
}
