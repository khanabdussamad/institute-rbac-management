package com.nagarro.rbacdemo.exception;

public class PrivilegeNotFoundException extends RuntimeException {

    public PrivilegeNotFoundException(String message) {
        super(message);
    }
}
