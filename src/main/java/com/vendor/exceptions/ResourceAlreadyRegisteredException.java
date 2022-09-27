package com.vendor.exceptions;

public class ResourceAlreadyRegisteredException extends RuntimeException {

    public ResourceAlreadyRegisteredException() {
        super("Resource already registered.");
    }

    public ResourceAlreadyRegisteredException(String resource) {
        super(String.format("%s already registered.", resource));
    }
}