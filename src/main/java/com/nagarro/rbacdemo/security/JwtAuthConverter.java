package com.nagarro.rbacdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthConverter {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> result = new ArrayList<>();

            Object rolesClaim = jwt.getClaim("roles");
            if (rolesClaim instanceof Collection<?> roles) {
                for (Object r : roles) {
                    result.add(new SimpleGrantedAuthority(String.valueOf(r))); // Keep ROLE_ prefix
                }
            }

            Object privsClaim = jwt.getClaim("privileges");
            if (privsClaim instanceof Collection<?> privs) {
                for (Object p : privs) {
                    result.add(new SimpleGrantedAuthority(String.valueOf(p)));
                }
            }

            return result;
        });
        return converter;
    }
}
