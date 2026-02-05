package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AppJwtService {
    private final JwtEncoder encoder;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long accessTtlSeconds;

    public AppJwtService(JwtEncoder encoder, RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
        this.encoder = encoder;
    }

    public Map<String, String> generateAccessToken(UserDetails userDetails) {
        Instant now = Instant.now();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTtlSeconds))
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .id(UUID.randomUUID().toString())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String accessToken = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("accessToken", accessToken);
        tokenResponse.put("tokenType", "Bearer");
        tokenResponse.put("expiresIn", String.valueOf(accessTtlSeconds));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        tokenResponse.put("refreshToken", refreshToken.getToken());
        return tokenResponse;
    }
}
