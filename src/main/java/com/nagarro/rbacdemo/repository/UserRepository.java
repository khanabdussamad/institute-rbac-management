package com.nagarro.rbacdemo.repository;

import com.nagarro.rbacdemo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "roles.privileges"})
    Optional<User> findWithRolesByUsername(String username);
}
