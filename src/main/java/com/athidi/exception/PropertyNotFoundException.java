package com.athidi.exception;

public class PropertyNotFoundException extends ResourceNotFoundException {
    public PropertyNotFoundException(String message) {
        super(message);
    }
}
