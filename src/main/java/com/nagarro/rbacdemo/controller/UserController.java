package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.ApiResponse;
import com.nagarro.rbacdemo.dto.UserRequest;
import com.nagarro.rbacdemo.dto.UserResponse;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<UserResponse>> getAll() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody UserRequest request) {
        User created = userService.create(request);
        UserResponse userResponse = UserResponse.builder()
                .email(created.getEmail())
                .username(created.getUsername())
                .userType(created.getUserType())
                .firstName(created.getFirstName())
                .lastName(created.getLastName())
                .status(created.getStatus())
                .build();
        return ApiResponse.success(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @RequestBody UserRequest request) {
        UserResponse updated = userService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
