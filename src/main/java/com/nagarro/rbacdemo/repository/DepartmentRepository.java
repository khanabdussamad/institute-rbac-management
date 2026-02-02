package com.nagarro.rbacdemo.repository;

import com.nagarro.rbacdemo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
}
