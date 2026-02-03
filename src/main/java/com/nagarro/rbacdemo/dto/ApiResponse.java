package com.nagarro.rbacdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Setter
@Getter
public class ApiResponse<T> {

    /** Indicates success (true) or failure (false) */
    private boolean status;

    /** Generic payload for successful responses */
    private T data;

    /** Optional error code for failures (default E101) */
    private String errorCode;

    /** Optional human-readable error message */
    private String errorMessage;

    // --- Success factories ---

    /**
     * Success response with default HTTP 200.
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .status(true)
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * Success response with custom HTTP status.
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus httpStatus) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .status(true)
                .data(data)
                .build();
        return ResponseEntity.status(httpStatus).body(body);
    }

    /**
     * No-data success convenience method (T optional) with default HTTP 200.
     */
    public static ResponseEntity<ApiResponse<Void>> success() {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(true)
                .data(null)
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * No-data success convenience method (T optional) with custom HTTP status.
     */
    public static ResponseEntity<ApiResponse<Void>> success(HttpStatus httpStatus) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(true)
                .data(null)
                .build();
        return ResponseEntity.status(httpStatus).body(body);
    }

    // --- Error factories (no generic param so T is optional) ---

    /**
     * Error response with default code E101 and HTTP 500.
     */
    public static ResponseEntity<ApiResponse<Void>> error(String errorMessage) {
        return error(errorMessage, "E101", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Error response with provided error code and default HTTP 500.
     */
    public static ResponseEntity<ApiResponse<Void>> error(String errorMessage, String errorCode) {
        return error(errorMessage, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Error response with provided error message, error code and HTTP status.
     */
    public static ResponseEntity<ApiResponse<Void>> error(String errorMessage, String errorCode, HttpStatus httpStatus) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(false)
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .build();
        return ResponseEntity.status(httpStatus).body(body);
    }
}