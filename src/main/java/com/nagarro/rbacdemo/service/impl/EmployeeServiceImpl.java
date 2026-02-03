package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.dto.EmployeeRequest;
import com.nagarro.rbacdemo.dto.EmployeeResponse;
import com.nagarro.rbacdemo.entity.Department;
import com.nagarro.rbacdemo.entity.Employee;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.exception.DepartmentNotFoundException;
import com.nagarro.rbacdemo.exception.EmployeeNotFoundException;
import com.nagarro.rbacdemo.exception.UserNotFoundException;
import com.nagarro.rbacdemo.repository.DepartmentRepository;
import com.nagarro.rbacdemo.repository.EmployeeRepository;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               UserRepository userRepository,
                               DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse findById(UUID id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return toResponse(e);
    }

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.getDepartmentId()));

        Employee e = new Employee();
        e.setUser(user);
        e.setDepartment(dept);
        e.setDesignation(request.getDesignation());
        e.setCreatedAt(LocalDateTime.now());

        Employee saved = employeeRepository.save(e);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.getDepartmentId()));

        e.setUser(user);
        e.setDepartment(dept);
        e.setDesignation(request.getDesignation());

        Employee saved = employeeRepository.save(e);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(e);
    }

    private EmployeeResponse toResponse(Employee e) {
        UUID userId = null;
        UUID departmentId = null;
        if (e.getUser() != null) userId = e.getUser().getId();
        if (e.getDepartment() != null) departmentId = e.getDepartment().getId();
        return new EmployeeResponse(e.getId(), userId, departmentId, e.getDesignation(), e.getCreatedAt());
    }
}
