package com.nagarro.rbacdemo.service;

public interface UserService {

    void resetPassword(String email, String newPassword);

    void deleteUser(Long userId);
}
