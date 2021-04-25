package com.helpserviceprovider.cartservice.exception;

public class CustomerIdEmptyException extends RuntimeException {
    public CustomerIdEmptyException(String message) {
        super(message);
    }
}
