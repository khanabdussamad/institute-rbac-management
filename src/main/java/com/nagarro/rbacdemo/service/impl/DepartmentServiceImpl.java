package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.dto.DepartmentRequest;
import com.nagarro.rbacdemo.dto.DepartmentResponse;
import com.nagarro.rbacdemo.entity.Department;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.exception.DepartmentNotFoundException;
import com.nagarro.rbacdemo.repository.DepartmentRepository;
import com.nagarro.rbacdemo.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public DepartmentResponse findById(UUID id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        return toResponse(dept);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        Department dept = new Department();
        dept.setName(request.getName());

        Department saved = departmentRepository.save(dept);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        dept.setName(request.getName());
        Department saved = departmentRepository.save(dept);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(dept);
    }

    private DepartmentResponse toResponse(Department d) {
        UUID createdById = null;
        User cb = d.getCreatedBy();
        if (cb != null) createdById = cb.getId();
        return new DepartmentResponse(d.getId(), d.getName(), createdById);
    }
}
