package com.vendor.services.impl;

import com.vendor.exceptions.NotFoundRoleException;
import com.vendor.exceptions.ResourceAlreadyRegisteredException;
import com.vendor.models.Role;
import com.vendor.models.User;
import com.vendor.repositories.RolesJpaRepository;
import com.vendor.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RolesJpaRepository repository;

    @Override
    public void save(Role role) {
        repository.save(role);
    }

    @Override
    public Role getRoleByName(String name) {
        Optional<Role> modalOptional = this.repository.findByName(name);
        if (!modalOptional.isPresent()) {
            throw new NotFoundRoleException(name);
        }

        return modalOptional.get();
    }
}
