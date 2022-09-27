package com.vendor.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponse {

    private String accessToken;
    private String message;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
