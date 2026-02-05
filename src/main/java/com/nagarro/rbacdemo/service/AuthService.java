package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.entity.RefreshToken;
import com.nagarro.rbacdemo.repository.RefreshTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final AppJwtService appJwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(AuthenticationManager authManager, AppJwtService appJwtService,
                       RefreshTokenService refreshTokenService, UserDetailsService userDetailsService,
                       RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.appJwtService = appJwtService;
        this.authManager = authManager;
    }

    public Map<String, String> login(String username, String rawPassword) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword)
        );
        return appJwtService.generateAccessToken((UserDetails) Objects.requireNonNull(auth.getPrincipal()));
    }

    @Transactional
    public Map<String, String> reGenerateAccessTokenWithRefreshToken(String refreshToken) {
        RefreshToken existing = refreshTokenService.verifyValid(refreshToken);

        // Load user details (you probably derive username via userId in your system)
        UserDetails user = userDetailsService.loadUserByUsername(existing.getUserId());

        // Rotate refresh token (recommended)
        existing.setRevoked(true);
        refreshTokenRepository.save(existing);

        RefreshToken rotated = refreshTokenService.createRefreshToken(existing.getUserId());

        Map<String, String> tokenData = appJwtService.generateAccessToken(user);
        tokenData.put("refresh_token", rotated.getToken());

        return tokenData;
    }


}
