package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.ApiResponse;
import com.nagarro.rbacdemo.dto.LoginRequest;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.security.JwtTokenProvider;
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

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // load user entity to include username and email claims
        var userOpt = userService.findByUsername(authentication.getName());

        var token = userOpt.map(user -> tokenProvider.generateToken(user))
                .orElseGet(() -> tokenProvider.generateTokenFromAuth(authentication));

        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        return ApiResponse.success(data);
    }

}