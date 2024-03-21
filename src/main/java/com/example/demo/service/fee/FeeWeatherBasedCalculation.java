package com.example.demo.service.fee;

import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.utils.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class FeeWeatherBasedCalculation implements FeeWeatherBased {
    @Override
    public FeeCalculationResponse calculate(double airTemperature, double windSpeed, String phenomenon, String city, VehicleType vehicleType) 
    {
        CityFee cityFee;
        switch (city) {
            case "TALLINN" -> cityFee = new TallinnFees();
            case "TARTU" -> cityFee = new TartuFees();
            case "PÄRNU" -> cityFee = new ParnuFees();
            default -> {
                return new FeeCalculationResponse("Unsupported city: " + city);
            }
        }
        double baseFee = cityFee.mapFee(vehicleType);

        FeeCalculationResponse response = new FeeCalculationResponse();

        if (vehicleType == VehicleType.BIKE) {
            if (windSpeed >= 10.0 && windSpeed <= 20.0) baseFee += 0.5;
            if (windSpeed > 20.0) {
                response.setErrorMessage("Usage of selected vehicle type is forbidden");
                return response;
            }
        }
        if (vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) {
            if (airTemperature < -10.0) baseFee += 1.0;
            if (airTemperature >= -10.0 && airTemperature <= 0) baseFee += 0.5;

            boolean hasSnow = phenomenon.contains("snow");
            boolean hasSleet = phenomenon.contains("sleet");
            boolean hasRain = phenomenon.contains("rain");
            // Only consider shower if snow hasn't been detected. Real cases: 'Light snow shower' 'Light shower'
            boolean hasShower = phenomenon.contains("shower") && !hasSnow;
            if (hasSnow || hasSleet) baseFee += 1;
            if (hasRain || hasShower) baseFee += 0.5;
            if (phenomenon.contains("Glaze") || phenomenon.contains("Hail") || phenomenon.contains("Thunder")) {
                response.setErrorMessage("Usage of selected vehicle type is forbidden");
                return response;
            }

        }

        response.setFee(baseFee);
        return response;
    }
}
