package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.dto.RoleRequest;
import com.nagarro.rbacdemo.dto.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    List<RoleResponse> findAll();

    RoleResponse findById(UUID id);

    RoleResponse create(RoleRequest request);

    RoleResponse update(UUID id, RoleRequest request);

    void delete(UUID id);
}
