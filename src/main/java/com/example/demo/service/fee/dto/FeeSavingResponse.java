package com.example.demo.service.fee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeSavingResponse {
    
    private boolean success;
    
    private String message;
    
    public static FeeSavingResponse success(String message) {
        return new FeeSavingResponse(true, message);
    }
    public static FeeSavingResponse failure(String message) {
        return new FeeSavingResponse(false, message);
    }
}
