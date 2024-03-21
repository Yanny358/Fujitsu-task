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
    
    private final FeeWeatherBased feeCalculation;

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
        return feeCalculation.calculate(latestAirTemperature, latestWindSpeed, latestPhenomenon,
                latestCity, type);
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
