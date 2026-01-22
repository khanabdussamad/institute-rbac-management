package com.nagarro.rbacdemo.dto;

import java.time.LocalDateTime;

public class ApiResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String status;
    private final String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}