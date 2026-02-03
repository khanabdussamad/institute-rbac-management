package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.dto.PrivilegeRequest;
import com.nagarro.rbacdemo.dto.PrivilegeResponse;

import java.util.List;
import java.util.UUID;

public interface PrivilegeService {

    List<PrivilegeResponse> findAll();

    PrivilegeResponse findById(UUID id);

    PrivilegeResponse create(PrivilegeRequest request);

    PrivilegeResponse update(UUID id, PrivilegeRequest request);

    void delete(UUID id);
}
