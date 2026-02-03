package com.nagarro.rbacdemo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtDecoderConfig {

    @Value("${app.jwt.secret}")
    private String appSecret;

    @Bean
    public JwtDecoder jwtDecoder() {

        NimbusJwtDecoder appJwtDecoder =
                NimbusJwtDecoder.withSecretKey(
                        new SecretKeySpec(appSecret.getBytes(), "HmacSHA256")
                ).build();

//        NimbusJwtDecoder keycloakJwtDecoder =
//                NimbusJwtDecoder.withJwkSetUri(
//                        "http://localhost:8080/realms/university-realm/protocol/openid-connect/certs"
//                ).build();

        return token -> {
            try {
                return appJwtDecoder.decode(token);
//                return keycloakJwtDecoder.decode(token);
            } catch (JwtException ex) {
                return appJwtDecoder.decode(token);
            }
        };
    }
}
