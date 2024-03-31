package com.example.demo.validationError;

public class WindFeeValidationException extends RuntimeException {
    public WindFeeValidationException(String message) {
        super(message);
    }
}
