package com.vendor.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserResponse {

    private String username;
    private Wallet wallet;
    private List<Role> roles;
}
