package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.ApiResponse;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.security.keycloak.KeycloakTokenService;
import com.nagarro.rbacdemo.security.keycloak.RbacProvisioningService;
import com.nagarro.rbacdemo.service.SSOJwtAuthService;
import com.nagarro.rbacdemo.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/sso")
public class SsoAuthController {

    private final KeycloakTokenService keycloakTokenService;
    private final RbacProvisioningService rbacProvisioningService;
    private final SSOJwtAuthService ssoJwtAuthService;
    private final UserService userService;

    public SsoAuthController(KeycloakTokenService keycloakTokenService,
                             RbacProvisioningService rbacProvisioningService,
                             SSOJwtAuthService ssoJwtAuthService,
                             UserService userService) {
        this.userService = userService;
        this.keycloakTokenService = keycloakTokenService;
        this.rbacProvisioningService = rbacProvisioningService;
        this.ssoJwtAuthService = ssoJwtAuthService;
    }

    /**
     * Accepts Keycloak token in Authorization header:
     * Authorization: Bearer <keycloak_id_or_access_token>
     */
    @PostMapping("/keycloak")
    public ResponseEntity<?> loginWithKeycloak(@RequestHeader("Keycloak-Token") String ssoToken) {
        if (ssoToken == null || !ssoToken.startsWith("Bearer ")) {
            return ApiResponse.error("Missing Bearer token", "E401", HttpStatus.BAD_REQUEST);
        }
        String externalToken = ssoToken.substring(7);

        // 1) Validate Keycloak token
        Jwt jwt = keycloakTokenService.decodeAndValidate(externalToken);

        // 2) Extract identity (claims differ by realm setup)
        String email = coalesce(jwt.getClaimAsString("email"),
                jwt.getClaimAsString("preferred_username"),
                jwt.getSubject());
        String username = coalesce(jwt.getClaimAsString("preferred_username"), email);
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        // 3) Ensure local user & default role
        rbacProvisioningService.ensureUserWithDefaultRole(username, email, firstName, lastName);

        Optional<User> userOptional = userService.findByUsername(username);

        // 4) Issue your app JWT (with roles & privileges)
        Map<String, String> appToken = ssoJwtAuthService.issueTokenFor(userOptional.get());

        return ApiResponse.success(appToken);
    }

    private static String coalesce(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }
}

