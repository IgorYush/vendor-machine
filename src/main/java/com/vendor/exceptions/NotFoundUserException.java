package com.vendor.exceptions;

public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException() {
        super("Not found user exception.");
    }

    public NotFoundUserException(String resource) {
        super(String.format("%s user not found.", resource));
    }
}
