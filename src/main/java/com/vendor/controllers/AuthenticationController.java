package com.vendor.controllers;

import com.vendor.config.jwt.JwtTokenUtil;
import com.vendor.models.ActiveUserStore;
import com.vendor.models.LoginResponse;
import com.vendor.models.LogoutTokensStore;
import com.vendor.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    LogoutTokensStore logoutTokensStore;

    @GetMapping("/logout/all")
    public ResponseEntity<?> logoutAll(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String bearer = request.getHeader("Authorization");
        String token = bearer.split(" ")[1].trim();

        User user = (User) auth.getPrincipal();

        if(activeUserStore.getActiveUsers().containsKey(user.getUsername())) {

            for(String authToken: activeUserStore.getActiveUsers().get(user.getUsername())) {
                logoutTokensStore.getTokens().add(authToken);
            }

            activeUserStore.getActiveUsers().get(user.getUsername()).clear();

            activeUserStore.getActiveUsers().remove(user.getUsername());
        }
        var a = activeUserStore.getActiveUsers().get(user.getUsername());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String bearer = request.getHeader("Authorization");
        String token = bearer.split(" ")[1].trim();

        User user = (User) auth.getPrincipal();

        if(!logoutTokensStore.getTokens().contains(token)) {
            logoutTokensStore.getTokens().add(token);
        }

        if(activeUserStore.getActiveUsers().containsKey(user.getUsername())) {
            activeUserStore.getActiveUsers().get(user.getUsername()).remove(token);
            if(activeUserStore.getActiveUsers().get(user.getUsername()).isEmpty()) {
                activeUserStore.getActiveUsers().remove(user.getUsername());
            }
        }

        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        SecurityContextHolder.clearContext();

        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);

            String message = "OK";
            if(!activeUserStore.getActiveUsers().containsKey(user.getUsername())) {
                List<String> tokens = new ArrayList<>();
                tokens.add(accessToken);
                activeUserStore.getActiveUsers().put(user.getUsername(), tokens);
            } else {
                activeUserStore.getActiveUsers().get(user.getUsername()).add(accessToken);
                message = "There is already an active session using your account.";
            }
            return ResponseEntity.ok().body(new LoginResponse(accessToken, message));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
