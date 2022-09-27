package com.vendor.services;

import com.vendor.models.User;
import com.vendor.models.Wallet;

import java.util.List;

public interface UserService {

    void save(User user);
    List<User> getAllUsers();

    User getUserByName(String roleName);

    void addRoleToUser(String username, String role);

    void deposit(Integer value, User u);

    void reset(User u);

    Integer getUserAmount(User u);
}