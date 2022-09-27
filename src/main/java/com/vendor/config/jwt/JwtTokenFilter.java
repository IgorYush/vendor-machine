package com.vendor.config.jwt;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vendor.models.LogoutTokensStore;
import com.vendor.models.Role;
import com.vendor.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private LogoutTokensStore logoutTokensStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);

        if(!logoutTokensStore.getTokens().contains(token)) {


            if (!jwtUtil.validateAccessToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            setAuthenticationContext(token, request);
            filterChain.doFilter(request, response);
        }else {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "UNAUTHORIZED"
                );
                filterChain.doFilter(request, response);
            }

    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        Claims claims = jwtUtil.parseClaims(token);
        String subject = (String) claims.get(Claims.SUBJECT);
        List<String> arrayRoles = (List<String>) claims.get("roles");

        for (String aRoleName : arrayRoles) {
            userDetails.addRole(new Role(aRoleName));
        }

        String[] jwtSubject = subject.split(",");

        userDetails.setId(UUID.fromString(jwtSubject[0]));
        userDetails.setUsername(jwtSubject[1]);

        return userDetails;
    }
}

