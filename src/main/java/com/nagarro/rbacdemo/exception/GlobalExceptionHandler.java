package com.nagarro.rbacdemo.exception;

import com.nagarro.rbacdemo.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(BadCredentialsException ex) {
        return ApiResponse.error("User not found", "E101", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPassword(InvalidPasswordException ex) {
        return ApiResponse.error("Invalid credentials", "E102", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        return ApiResponse.error("Department not found", "E101", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthorizationDeniedException ex) {
        return ApiResponse.error("You are not authorise for this", "E101", HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.info(ex.getMessage());
        return ApiResponse.error("Oops something went wrong, try again later", "E101", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}