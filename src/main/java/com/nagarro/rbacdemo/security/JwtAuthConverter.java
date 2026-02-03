package com.nagarro.rbacdemo.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // App JWT
        List<String> roles = jwt.getClaimAsStringList("role");
        if (roles != null) {
            roles.forEach(r ->
                    authorities.add(new SimpleGrantedAuthority(r))
            );
        }

        // Keycloak JWT
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            List<String> kcRoles = (List<String>) realmAccess.get("roles");
            kcRoles.forEach(r ->
                    authorities.add(new SimpleGrantedAuthority(r))
            );
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
