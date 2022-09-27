package com.vendor.services;

import com.vendor.models.BuyReturnDTO;
import com.vendor.models.Product;
import com.vendor.models.User;

import java.util.List;

public interface ProductService {

    Product getByName(String name);
    List<Product> getAll();
    void delete(String name, User creator);
    void save(Product product, User creator);

    void update(String name, Product product, User creator);

    BuyReturnDTO buy(String name, Integer amount, User u);
}
