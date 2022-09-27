package com.vendor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BuyReturnDTO {

    private String productName;
    private List<Integer> change;
}
