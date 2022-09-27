package com.vendor.exceptions;

public class TypeOfCoinNotAcceptedException extends RuntimeException {

    public TypeOfCoinNotAcceptedException() {
        super("Type of coin not accepted exception.");
    }
}
