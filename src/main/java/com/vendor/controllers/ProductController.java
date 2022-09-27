package com.vendor.controllers;


import com.vendor.models.BuyReturnDTO;
import com.vendor.models.Product;
import com.vendor.models.User;
import com.vendor.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductService service;

    @GetMapping("")
    public ResponseEntity<Product> getByName(@RequestParam String name) {
        return ResponseEntity.ok(this.service.getByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(this.service.getAll());
    }

    @RolesAllowed({"ROLE_SELLER", "ROLE_ADMIN"})
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody Product product) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.service.save(product, u);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed({"ROLE_SELLER", "ROLE_ADMIN"})
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam String name) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.service.delete(name, u);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RolesAllowed({"ROLE_SELLER", "ROLE_ADMIN"})
    @PutMapping("/update")
    public ResponseEntity<Void> save(@RequestParam String name, @RequestBody Product product) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.service.update(name, product, u);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @RolesAllowed({"ROLE_BUYER", "ROLE_ADMIN"})
    @PostMapping("/buy")
    public ResponseEntity<BuyReturnDTO> buy(@RequestParam String name, @RequestParam Integer amount) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BuyReturnDTO dto = this.service.buy(name, amount, u);
        return ResponseEntity.ok(dto);
    }
}
