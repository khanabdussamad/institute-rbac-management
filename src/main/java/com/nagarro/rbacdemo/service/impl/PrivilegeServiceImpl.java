package com.nagarro.rbacdemo.service.impl;

import com.nagarro.rbacdemo.dto.PrivilegeRequest;
import com.nagarro.rbacdemo.dto.PrivilegeResponse;
import com.nagarro.rbacdemo.entity.Privilege;
import com.nagarro.rbacdemo.exception.PrivilegeNotFoundException;
import com.nagarro.rbacdemo.repository.PrivilegeRepository;
import com.nagarro.rbacdemo.service.PrivilegeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private static final Logger log = LoggerFactory.getLogger(PrivilegeServiceImpl.class);

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<PrivilegeResponse> findAll() {
        return privilegeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PrivilegeResponse findById(UUID id) {
        Privilege p = privilegeRepository.findById(id)
                .orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found with id: " + id));
        return toResponse(p);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PrivilegeResponse create(PrivilegeRequest request) {
        // ensure unique name
        privilegeRepository.findByName(request.getName()).ifPresent(p -> {
            throw new IllegalArgumentException("Privilege with name already exists: " + request.getName());
        });

        Privilege p = new Privilege();
        p.setName(request.getName());
        p.setResource(request.getResource());
        p.setAction(request.getAction());

        Privilege saved = privilegeRepository.save(p);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PrivilegeResponse update(UUID id, PrivilegeRequest request) {
        Privilege p = privilegeRepository.findById(id)
                .orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found with id: " + id));
        p.setName(request.getName());
        p.setResource(request.getResource());
        p.setAction(request.getAction());
        Privilege saved = privilegeRepository.save(p);
        return toResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Privilege p = privilegeRepository.findById(id)
                .orElseThrow(() -> new PrivilegeNotFoundException("Privilege not found with id: " + id));
        privilegeRepository.delete(p);
    }

    private PrivilegeResponse toResponse(Privilege p) {
        return new PrivilegeResponse(p.getId(), p.getName(), p.getResource(), p.getAction());
    }
}
