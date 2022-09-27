package com.vendor.controllers;

import com.vendor.models.Role;
import com.vendor.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
public class RoleController {

    private RoleService service;

    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping("")
    public ResponseEntity<Void> saveRole(@RequestBody Role role) {
        this.service.save(role);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
