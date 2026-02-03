package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.ApiResponse;
import com.nagarro.rbacdemo.dto.LoginRequest;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.security.JwtTokenProvider;
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
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // load user entity to include username and email claims
        var userOpt = userRepository.findByEmail(authentication.getName());

        var token = userOpt.map(user -> tokenProvider.generateToken(user))
                .orElseGet(() -> tokenProvider.generateTokenFromAuth(authentication));

        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");

        return ApiResponse.success(data);
    }

}