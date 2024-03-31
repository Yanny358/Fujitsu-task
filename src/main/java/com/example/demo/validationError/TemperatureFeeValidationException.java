package com.example.demo.validationError;

public class TemperatureFeeValidationException extends RuntimeException{
    public TemperatureFeeValidationException(String message) {
        super(message);
    }
}
