package com.example.demo.controller;

import com.example.demo.service.fee.FeeService;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class FeeController {
    
    private final FeeService feeService;

    @GetMapping("/calculateFee/{city}/{vehicleType}")
    public ResponseEntity<FeeCalculationResponse> calculateFee(@PathVariable String city, @PathVariable String vehicleType) {
        try {
            FeeCalculationResponse response = feeService.findWeatherStationAndCalculateFee(city, vehicleType);
            if (response.getErrorMessage() != null) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new FeeCalculationResponse(
                    "Unable to calculate fee" + e.getMessage()));
        }
    }
}
