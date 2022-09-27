package com.vendor.exceptions;

public class NotFoundGeneralException extends RuntimeException {

    public NotFoundGeneralException() {
        super("Not found exception.");
    }

    public NotFoundGeneralException(String resource) {
        super(String.format("%s not found.", resource));
    }
}
