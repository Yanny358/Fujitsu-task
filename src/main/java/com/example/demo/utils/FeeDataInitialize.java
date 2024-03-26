package com.example.demo.utils;

import com.example.demo.service.fee.FeeWeatherBasedCalculation;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class FeeDataInitialize {
    private final FeeWeatherBasedCalculation feeWeatherBasedCalculation;

    public FeeDataInitialize(FeeWeatherBasedCalculation feeWeatherBasedCalculation) {
        this.feeWeatherBasedCalculation = feeWeatherBasedCalculation;
    }

    @PostConstruct
    public void initializeDefaultFees() {
        feeWeatherBasedCalculation.saveDefaultFees();
    }
}
