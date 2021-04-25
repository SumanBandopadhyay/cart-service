package com.helpserviceprovider.cartservice.exception;

public class CartDoesNotExistsException extends RuntimeException {

    public CartDoesNotExistsException(String message) {
        super(message);
    }
}
