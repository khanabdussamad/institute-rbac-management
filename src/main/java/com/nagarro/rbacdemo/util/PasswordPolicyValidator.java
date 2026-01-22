package com.nagarro.rbacdemo.util;

import org.springframework.stereotype.Component;

@Component
public class PasswordPolicyValidator {
    public boolean isValid(String password) {
        return password != null && password.length() >= 8;
    }
}