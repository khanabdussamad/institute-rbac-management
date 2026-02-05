package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.ApiResponse;
import com.nagarro.rbacdemo.dto.LoginRequest;
import com.nagarro.rbacdemo.dto.TokenRefreshRequest;
import com.nagarro.rbacdemo.service.AppJwtService;
import com.nagarro.rbacdemo.service.AuthService;
import com.nagarro.rbacdemo.service.RefreshTokenService;
import com.nagarro.rbacdemo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@Tag(name = "Auth", description = "Operations about authentication")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final AppJwtService appJwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService, RefreshTokenService refreshTokenService, AppJwtService appJwtService) {
       this.appJwtService = appJwtService;
        this.refreshTokenService = refreshTokenService;
       this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, String> tokens = authService.login(request.getUsername(), request.getPassword());
        return ApiResponse.success(tokens);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Map<String, String>>> refresh(@RequestBody TokenRefreshRequest request) {
        Map<String, String> tokenData = authService.reGenerateAccessTokenWithRefreshToken(request.getRefreshToken());
        return ApiResponse.success(tokenData);
    }

    @PostMapping("/logout")
    public void logout(@RequestParam("userId") String userId) {
        refreshTokenService.revokeAllForUser(userId);
    }

}