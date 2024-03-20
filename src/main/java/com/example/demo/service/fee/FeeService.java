package com.example.demo.service.fee;

import com.example.demo.model.WeatherStation;
import com.example.demo.repository.WeatherStationRepository;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.utils.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final WeatherStationRepository weatherStationRepository;

    public FeeCalculationResponse findWeatherStationAndCalculateFee(String station, String vehicleType) {
        VehicleType type;
        try {
            type = VehicleType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new FeeCalculationResponse("Invalid vehicle type: " + vehicleType);
        }
        
        Optional<WeatherStation> latestStation = weatherStationRepository.findLatestByStationName(cityNameToStationName(station));

        if (latestStation.isEmpty()) {
            return new FeeCalculationResponse( "No weather station data available for " + station);
        }
        String latestCity = stationNameToCityName(latestStation.get().getName());
        double latestAirTemperature = latestStation.get().getAirTemperature();
        double latestWindSpeed = latestStation.get().getWindSpeed();
        String latestPhenomenon = latestStation.get().getWeatherPhenomenon();
        return calculateFeeOnCityAndWeatherConditions(latestAirTemperature, latestWindSpeed, latestPhenomenon,
                latestCity, type);
    }

    private FeeCalculationResponse calculateFeeOnCityAndWeatherConditions(double airTemperature, double windSpeed,
                                                                          String phenomenon, String city,
                                                                          VehicleType vehicleType) {
        CityFee cityFee;
        switch (city) {
            case "TALLINN" -> cityFee = new TallinnFees();
            case "TARTU" -> cityFee = new TartuFees();
            case "PÄRNU" -> cityFee = new ParnuFees();
            default -> {
                return null;
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
            // Only consider shower if snow hasn't been detected. Example 'Light snow shower' 'Light shower'
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

    private String cityNameToStationName(String cityName) {
        return switch (cityName.toUpperCase()) {
            case "TARTU" -> "Tartu-Tõravere";
            case "TALLINN" -> "Tallinn-Harku";
            case "PÄRNU" -> "Pärnu";
            default -> null;
        };
    }

    private String stationNameToCityName(String stationName) {
        return switch (stationName) {
            case "Tartu-Tõravere" -> "TARTU";
            case "Tallinn-Harku" -> "TALLINN";
            case "Pärnu" -> "PÄRNU";
            default -> stationName;
        };
    }
}
