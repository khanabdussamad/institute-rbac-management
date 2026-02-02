package com.nagarro.rbacdemo.service;

import java.util.UUID;

public interface UserService {

    void resetPassword(String email, String newPassword);

    void deleteUser(UUID userId);
}
