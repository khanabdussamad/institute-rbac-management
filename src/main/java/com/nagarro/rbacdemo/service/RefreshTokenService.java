package com.nagarro.rbacdemo.service;


import com.nagarro.rbacdemo.entity.RefreshToken;
import com.nagarro.rbacdemo.exception.TokenRefreshException;
import com.nagarro.rbacdemo.repository.RefreshTokenRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;

    // Customize TTLs as you like
    private final Duration refreshTokenTtl = Duration.ofDays(7);    // long-lived refresh

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserDetailsService userDetailsService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
    }

    public RefreshToken createRefreshToken(String username) {
        String token = generateSecureRandomToken();
        Instant expiry = Instant.now().plus(refreshTokenTtl);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userId(username)
                .expiryAt(expiry)
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyValid(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Invalid refresh token"));

        if (rt.isRevoked()) {
            throw new TokenRefreshException("Refresh token has been revoked");
        }
        if (rt.getExpiryAt().isBefore(Instant.now())) {
            // Optionally delete it
            refreshTokenRepository.delete(rt);
            throw new TokenRefreshException("Refresh token has expired");
        }
        return rt;
    }

    @Transactional
    public void revokeAllForUser(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }


    private String generateSecureRandomToken() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

