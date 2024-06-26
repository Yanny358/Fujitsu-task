package com.example.demo.service.fee;

import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.model.enums.VehicleType;

public interface FeeWeatherBased {
    FeeCalculationResponse calculate(
            double airTemperature, double windSpeed, String phenomenon, String city, VehicleType vehicleType);
}
