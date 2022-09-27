package com.vendor.controllers;

import com.vendor.mapper.UserDTOMapper;
import com.vendor.models.User;
import com.vendor.models.UserResponse;
import com.vendor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@DeclareRoles({"ROLE_ADMIN", "ROLE_USER"})
public class UserController {

    private UserService userService;

    @Autowired private UserDTOMapper mapper;

    public UserController(UserService userService, UserDTOMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> saveUser(@RequestBody User user) {
        this.userService.save(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = this.userService.getAllUsers().stream().map(mapper::modelToDto).toList();
        return ResponseEntity.ok(users);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping("/add/role")
    public ResponseEntity<Void> addRoleToUser(@RequestBody Map<String, String> json) {
        this.userService.addRoleToUser(json.get("username"), json.get("roleName"));
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("")
    public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(mapper.modelToDto(this.userService.getUserByName(username)));
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_BUYER"})
    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestParam Integer value) {

        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.userService.deposit(value, u);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_BUYER"})
    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {

        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        this.userService.reset(u);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_BUYER"})
    @GetMapping("/wallet")
    public ResponseEntity<Integer> getUserAmount() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(this.userService.getUserAmount(u));
    }
}
