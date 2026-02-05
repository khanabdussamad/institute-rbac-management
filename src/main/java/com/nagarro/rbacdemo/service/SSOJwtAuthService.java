package com.nagarro.rbacdemo.service;

import com.nagarro.rbacdemo.entity.Privilege;
import com.nagarro.rbacdemo.entity.Role;
import com.nagarro.rbacdemo.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SSOJwtAuthService {

    private final JwtEncoder encoder;

    @Value("${app.jwt.issuer}")
    private String issuer;
    @Value("${app.jwt.expiration-seconds}")
    private long ttl;

    public SSOJwtAuthService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public Map<String, String> issueTokenFor(User user) {
        // Collect roles & privileges from user’s roles
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> privileges = user.getRoles().stream()
                .flatMap(r -> r.getPrivileges().stream())
                .map(Privilege::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ttl))
                .subject(user.getEmail())  // or user.getUsername()
                .claim("roles", roles)
                .claim("privileges", privileges)
                .id(UUID.randomUUID().toString())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String accessToken = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("accessToken", accessToken);
        tokenResponse.put("tokenType", "Bearer");
        tokenResponse.put("expiresIn", ttl + "");
        return tokenResponse;
    }
}