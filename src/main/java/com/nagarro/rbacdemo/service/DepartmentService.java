package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.dto.DepartmentRequest;
import com.nagarro.rbacdemo.dto.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    List<DepartmentResponse> findAll();

    DepartmentResponse findById(UUID id);

    DepartmentResponse create(DepartmentRequest request);

    DepartmentResponse update(UUID id, DepartmentRequest request);

    void delete(UUID id);
}
