package com.vendor.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class ActiveUserStore {

    public Map<String, List<String>> activeUsers;

    public ActiveUserStore() {
        activeUsers = new HashMap<>();
    }
}
