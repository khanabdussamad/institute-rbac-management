package com.nagarro.rbacdemo.security.keycloak;


import com.nagarro.rbacdemo.dto.UserRequest;
import com.nagarro.rbacdemo.entity.Role;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.repository.RoleRepository;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RbacProvisioningService {
    private final UserService userService;
    private final RoleRepository roleRepo;

    public RbacProvisioningService(UserService userService, RoleRepository roleRepo) {
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @Transactional(rollbackOn = Exception.class)
    public void ensureUserWithDefaultRole(String username, String email, String firstName, String lastName) {
        // 1) Ensure role exists
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    r.setDescription("SSO user");
                    return roleRepo.save(r);
                });

        // 2) Ensure user exists
        Optional<User> byEmail = userService.findByUsername(username);
        User user = byEmail.orElseGet(() -> {
            UserRequest u = new UserRequest();
            u.setUsername(username != null ? username : email);
            u.setEmail(email);
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setStatus("Active");
            u.setAuthProvider("SSO Keycloak");
            u.setUserType("SSO");
            u.setRoles(Set.of(userRole));
            return userService.create(u);
        });
    }
}

