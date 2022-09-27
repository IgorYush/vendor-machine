package com.vendor.exceptions;

public class NotEnoughFundsException extends RuntimeException {

    public NotEnoughFundsException() {
        super("Not enough funds exception.");
    }
}