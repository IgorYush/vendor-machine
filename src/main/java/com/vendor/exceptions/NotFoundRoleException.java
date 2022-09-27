package com.vendor.exceptions;

public class NotFoundRoleException extends RuntimeException {

    public NotFoundRoleException() {
        super("Not found role exception.");
    }

    public NotFoundRoleException(String resource) {
        super(String.format("%s role not found.", resource));
    }
}