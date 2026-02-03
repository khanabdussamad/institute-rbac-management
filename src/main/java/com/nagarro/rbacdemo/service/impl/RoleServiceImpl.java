package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.dto.RoleRequest;
import com.nagarro.rbacdemo.dto.RoleResponse;
import com.nagarro.rbacdemo.entity.Privilege;
import com.nagarro.rbacdemo.entity.Role;
import com.nagarro.rbacdemo.exception.RoleNotFoundException;
import com.nagarro.rbacdemo.repository.PrivilegeRepository;
import com.nagarro.rbacdemo.repository.RoleRepository;
import com.nagarro.rbacdemo.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<RoleResponse> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RoleResponse findById(UUID id) {
        Role r = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        return toResponse(r);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleResponse create(RoleRequest request) {
        Role r = new Role();
        r.setName(request.getName());
        r.setDescription(request.getDescription());
        r.setPrivileges(fetchPrivileges(request.getPrivilegeIds()));
        Role saved = roleRepository.save(r);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleResponse update(UUID id, RoleRequest request) {
        Role r = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        r.setName(request.getName());
        r.setDescription(request.getDescription());
        r.setPrivileges(fetchPrivileges(request.getPrivilegeIds()));
        Role saved = roleRepository.save(r);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Role r = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        roleRepository.delete(r);
    }

    private Set<Privilege> fetchPrivileges(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream()
                .map(privilegeRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toSet());
    }

    private RoleResponse toResponse(Role r) {
        Set<UUID> privilegeIds = null;
        if (r.getPrivileges() != null) {
            privilegeIds = r.getPrivileges().stream()
                    .map(Privilege::getId)
                    .collect(Collectors.toSet());
        }
        return new RoleResponse(r.getId(), r.getName(), r.getDescription(), privilegeIds);
    }
}
