package com.nagarro.rbacdemo.security;

import com.nagarro.rbacdemo.entity.Privilege;
import com.nagarro.rbacdemo.entity.Role;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

// Aggregate authorities: ROLE_* + PRIVILEGE strings
        Set<GrantedAuthority> authorities = new HashSet<>();


        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName())); // e.g., ROLE_ADMIN
            for (Privilege p : role.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(p.getName()));  // e.g., ORDER_READ
            }
        }


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(!user.getStatus().equals("ACTIVE"))
                .accountLocked(!user.getStatus().equals("ACTIVE"))
                .credentialsExpired(!user.getStatus().equals("ACTIVE"))
                .disabled(!user.getStatus().equals("ACTIVE"))
                .build();



    }
}