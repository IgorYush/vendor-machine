package com.vendor.services;

import com.vendor.models.Role;

public interface RoleService {

    void save(Role role);

    Role getRoleByName(String name);
}
