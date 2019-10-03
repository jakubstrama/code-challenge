package com.hsbc.codechallenge.exceptions.runtime;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
