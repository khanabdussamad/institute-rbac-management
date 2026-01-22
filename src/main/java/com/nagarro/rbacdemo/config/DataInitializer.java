package com.nagarro.rbacdemo.config;

import com.nagarro.rbacdemo.entity.Role;
import com.nagarro.rbacdemo.entity.User;
import com.nagarro.rbacdemo.repository.RoleRepository;
import com.nagarro.rbacdemo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
            User admin = new User(null, "admin@test.com",
                    passwordEncoder.encode("admin123"), true, Set.of(adminRole));
            userRepository.save(admin);
        };
    }
}