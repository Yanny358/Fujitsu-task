package com.example.demo.validationError;

public class PhenomenonFeeValidationException extends RuntimeException{
    public PhenomenonFeeValidationException(String message) {
        super(message);
    }
}
