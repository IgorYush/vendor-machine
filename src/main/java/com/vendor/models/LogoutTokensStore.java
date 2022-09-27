package com.vendor.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LogoutTokensStore {

    private List<String> tokens;

    public LogoutTokensStore() {
        this.tokens = new ArrayList<>();
    }
}
