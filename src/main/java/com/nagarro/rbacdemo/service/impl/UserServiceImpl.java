package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.exception.InvalidPasswordException;
import com.nagarro.rbacdemo.exception.UserDeletionNotAllowedException;
import com.nagarro.rbacdemo.exception.UserNotFoundException;
import com.nagarro.rbacdemo.repository.UserRepository;
import com.nagarro.rbacdemo.service.UserService;
import com.nagarro.rbacdemo.util.PasswordPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyValidator passwordPolicyValidator;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           PasswordPolicyValidator passwordPolicyValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordPolicyValidator = passwordPolicyValidator;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successful for email={}", email);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {

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
}