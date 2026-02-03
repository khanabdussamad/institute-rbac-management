package com.nagarro.rbacdemo.controller;

import com.nagarro.rbacdemo.dto.PrivilegeRequest;
import com.nagarro.rbacdemo.dto.PrivilegeResponse;
import com.nagarro.rbacdemo.service.PrivilegeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping
    public ResponseEntity<List<PrivilegeResponse>> getAll() {
        return ResponseEntity.ok(privilegeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(privilegeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PrivilegeResponse> create(@RequestBody PrivilegeRequest request) {
        PrivilegeResponse created = privilegeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrivilegeResponse> update(@PathVariable UUID id,
                                                     @RequestBody PrivilegeRequest request) {
        PrivilegeResponse updated = privilegeService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        privilegeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
