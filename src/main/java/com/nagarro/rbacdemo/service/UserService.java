package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.dto.UserRequest;
import com.nagarro.rbacdemo.dto.UserResponse;
import com.nagarro.rbacdemo.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void resetPassword(String email, String newPassword);

    void deleteUser(UUID userId);

    // CRUD methods for users
    List<UserResponse> findAll();

    UserResponse findById(UUID id);

    User create(UserRequest request);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);

    Optional<User> findByUsername(String username);
}
