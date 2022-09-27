package com.vendor.exceptions;

public class UserAlreadyHaveRoleException extends RuntimeException {

    public UserAlreadyHaveRoleException() {
        super("User already have role exception.");
    }

    public UserAlreadyHaveRoleException(String user, String role) {
        super(String.format("%s already have role %s.", user, role));
    }
}