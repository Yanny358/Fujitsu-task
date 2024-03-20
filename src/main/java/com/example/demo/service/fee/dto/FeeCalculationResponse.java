package com.example.demo.service.fee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // ignore null fields when serializing to JSON
public class FeeCalculationResponse {
    private Double fee;
    
    private String errorMessage;
    
    public FeeCalculationResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
