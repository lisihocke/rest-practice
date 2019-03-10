package com.lisihocke.restpractice.order;

public class OrderNotFoundException extends RuntimeException {

    OrderNotFoundException(Long id) {
        super("Could not find order " + id);
    }
}