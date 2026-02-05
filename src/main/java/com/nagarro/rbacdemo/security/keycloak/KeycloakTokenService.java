package com.nagarro.rbacdemo.security.keycloak;


import com.nagarro.rbacdemo.security.AudienceValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

@Component
public class KeycloakTokenService {

    @Value("${keycloak.issuer-uri}")
    String issuer;
    @Value("${keycloak.jwk-set-uri}")
    String jwkSetUri;
    @Value("${keycloak.audience}")
    String audience;


    public Jwt decodeAndValidate(String externalToken) {
        NimbusJwtDecoder nimbus = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Validate issuer + expiry + not before
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        // Validate audience (client_id)
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
        nimbus.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        return nimbus.decode(externalToken); // throws JwtException if invalid
    }
}

