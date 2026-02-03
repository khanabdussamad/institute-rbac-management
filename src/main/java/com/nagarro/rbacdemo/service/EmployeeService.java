package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.dto.EmployeeRequest;
import com.nagarro.rbacdemo.dto.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponse> findAll();

    EmployeeResponse findById(UUID id);

    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse update(UUID id, EmployeeRequest request);

    void delete(UUID id);
}
