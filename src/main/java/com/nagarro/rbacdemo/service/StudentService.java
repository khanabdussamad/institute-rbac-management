package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.dto.StudentRequest;
import com.nagarro.rbacdemo.dto.StudentResponse;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    List<StudentResponse> findAll();

    StudentResponse findById(UUID id);

    StudentResponse create(StudentRequest request);

    StudentResponse update(UUID id, StudentRequest request);

    void delete(UUID id);
}
