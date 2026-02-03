package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.dto.StudentRequest;
import com.nagarro.rbacdemo.dto.StudentResponse;
import com.nagarro.rbacdemo.entity.Department;
import com.nagarro.rbacdemo.entity.Student;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.exception.DepartmentNotFoundException;
import com.nagarro.rbacdemo.exception.StudentNotFoundException;
import com.nagarro.rbacdemo.exception.UserNotFoundException;
import com.nagarro.rbacdemo.repository.DepartmentRepository;
import com.nagarro.rbacdemo.repository.StudentRepository;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<StudentResponse> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public StudentResponse findById(UUID id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        return toResponse(s);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public StudentResponse create(StudentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.getDepartmentId()));

        Student s = new Student();
        s.setUser(user);
        s.setDepartment(dept);
        s.setRollNumber(request.getRollNumber());
        s.setCreatedAt(LocalDateTime.now());

        Student saved = studentRepository.save(s);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public StudentResponse update(UUID id, StudentRequest request) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.getDepartmentId()));

        s.setUser(user);
        s.setDepartment(dept);
        s.setRollNumber(request.getRollNumber());

        Student saved = studentRepository.save(s);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        studentRepository.delete(s);
    }

    private StudentResponse toResponse(Student s) {
        UUID userId = null;
        UUID departmentId = null;
        if (s.getUser() != null) userId = s.getUser().getId();
        if (s.getDepartment() != null) departmentId = s.getDepartment().getId();
        return new StudentResponse(s.getId(), userId, departmentId, s.getRollNumber(), s.getCreatedAt());
    }
}
