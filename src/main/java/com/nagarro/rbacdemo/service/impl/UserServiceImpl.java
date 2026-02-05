package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.dto.UserRequest;
import com.nagarro.rbacdemo.dto.UserResponse;
import com.nagarro.rbacdemo.entity.Privilege;
import com.nagarro.rbacdemo.entity.Role;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.exception.InvalidPasswordException;
import com.nagarro.rbacdemo.exception.UserDeletionNotAllowedException;
import com.nagarro.rbacdemo.exception.UserNotFoundException;
import com.nagarro.rbacdemo.repository.PrivilegeRepository;
import com.nagarro.rbacdemo.repository.RoleRepository;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.service.UserService;
import com.nagarro.rbacdemo.util.PasswordPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyValidator passwordPolicyValidator;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PrivilegeRepository privilegeRepository,
                           PasswordEncoder passwordEncoder,
                           PasswordPolicyValidator passwordPolicyValidator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordPolicyValidator = passwordPolicyValidator;
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        log.debug("Password reset initiated for email={}", email);
        if (!passwordPolicyValidator.isValid(newPassword)) {
            log.warn("Password policy failed for email={}", email);
            throw new InvalidPasswordException("Password does not meet security policy");
        }
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email={}", email);
                    return new UserNotFoundException("User not found: " +
                            email);

                });
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successful for email={}", email);
    }

    @Override
    public void deleteUser(UUID userId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String currentUserEmail = Objects.requireNonNull(authentication).getName();

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        if (Objects.equals(userToDelete.getEmail(), currentUserEmail)) {
            log.warn(
                    "Admin [{}] attempted to delete own account",
                    currentUserEmail
            );
            throw new UserDeletionNotAllowedException(
                    "You cannot delete your own account"
            );
        }

        userRepository.delete(userToDelete);

        log.info(
                "User [{}] deleted by admin [{}]",
                userToDelete.getEmail(),
                currentUserEmail
        );
    }

    // CRUD methods
    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findById(UUID id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return toResponse(u);
    }

    @Override
    @Transactional
    public User create(UserRequest request) {
        User u = new User();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setUserType(request.getUserType());
        u.setStatus(request.getStatus());
        // Ensure roles and privileges are persisted / resolved first to avoid transient instance errors
        Set<Role> incomingRoles = request.getRoles();
        Set<Role> persistedRoles = ensureRolesPersisted(incomingRoles);
        u.setRoles(persistedRoles);
        u.setAuthProvider(request.getAuthProvider());
        // Note: password/auth fields intentionally left to service or registration flow

        return userRepository.save(u);
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setUserType(request.getUserType());
        u.setStatus(request.getStatus());
        User saved = userRepository.save(u);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(u);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .userType(u.getUserType())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .status(u.getStatus())
                .build();
    }

    // --- helper methods ---
    private Set<Role> ensureRolesPersisted(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        Set<Role> persisted = new HashSet<>();
        for (Role r : roles) {
            if (r == null) continue;
            Optional<Role> existing = roleRepository.findByName(r.getName());
            if (existing.isPresent()) {
                persisted.add(existing.get());
            } else {
                Role newRole = new Role();
                newRole.setName(r.getName());
                newRole.setDescription(r.getDescription());
                // handle privileges
                Set<Privilege> incomingPrivileges = r.getPrivileges();
                Set<Privilege> savedPrivileges = new HashSet<>();
                if (incomingPrivileges != null) {
                    for (Privilege p : incomingPrivileges) {
                        if (p == null) continue;
                        Optional<Privilege> existingP = privilegeRepository.findByName(p.getName());
                        if (existingP.isPresent()) {
                            savedPrivileges.add(existingP.get());
                        } else {
                            Privilege np = new Privilege();
                            np.setName(p.getName());
                            np.setResource(p.getResource());
                            np.setAction(p.getAction());
                            Privilege saved = privilegeRepository.save(np);
                            savedPrivileges.add(saved);
                        }
                    }
                }
                newRole.setPrivileges(savedPrivileges);
                Role savedRole = roleRepository.save(newRole);
                persisted.add(savedRole);
            }
        }
        return persisted;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findWithRolesByUsername(username);
    }
}

