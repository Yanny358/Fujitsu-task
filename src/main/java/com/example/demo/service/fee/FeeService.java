package com.example.demo.service.fee;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.model.WeatherStation;
import com.example.demo.repository.CityAndVehicleFeeRepository;
import com.example.demo.repository.WeatherStationRepository;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.service.fee.dto.FeeSavingResponse;
import com.example.demo.utils.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing weather-based fee calculations and updates.
 * This service interacts with repositories to fetch weather station data and city and vehicle fee information,
 * performing calculations and updates as necessary based on provided parameters.
 */
@Service
@RequiredArgsConstructor
public class FeeService {

    private final WeatherStationRepository weatherStationRepository;

    private final FeeWeatherBased feeCalculation;

    private final CityAndVehicleFeeRepository cityAndVehicleFeeRepository;

    /**
     * Finds the latest weather station data for a given city and calculates the fee based on the vehicle type and current weather conditions.
     * If the vehicle type is invalid or no weather station data is available for the specified city, it returns an error message.
     *
     * @param city        The name of the city for which to find weather data and calculate the fee.
     * @param vehicleType The type of vehicle as a string. This will be converted to {@link VehicleType}.
     * @return {@link FeeCalculationResponse} containing the calculated fee or an error message.
     */
    public FeeCalculationResponse findWeatherStationAndCalculateFee(String city, String vehicleType) {
        VehicleType type;
        try {
            type = VehicleType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new FeeCalculationResponse("Invalid vehicle type: " + vehicleType);
        }

        city = city.toUpperCase();
        Optional<WeatherStation> latestStation = weatherStationRepository.findLatestByStationName(cityNameToStationName(city));

        if (latestStation.isEmpty()) {
            return new FeeCalculationResponse("No weather station data available for: " + city + ". It is " +
                    "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU.");
        }
        String latestCity = stationNameToCityName(latestStation.get().getName());
        double latestAirTemperature = latestStation.get().getAirTemperature();
        double latestWindSpeed = latestStation.get().getWindSpeed();
        String latestPhenomenon = latestStation.get().getWeatherPhenomenon();
        return feeCalculation.calculate(latestAirTemperature, latestWindSpeed, latestPhenomenon,
                latestCity, type);
    }

    /**
     * Updates the fee for a specific city and vehicle type combination. If the vehicle type is invalid,
     * it returns an error response. Otherwise, it updates the fee in the database.
     *
     * @param city        The city for which the fee is being updated.
     * @param vehicleType The type of vehicle as a string. This will be converted to {@link VehicleType}.
     * @param newFee      The new fee amount to be saved.
     * @return {@link FeeSavingResponse} indicating the success or failure of the operation along with an appropriate message.
     */
    public FeeSavingResponse updateBaseFee(String city, String vehicleType, double newFee) {
        VehicleType type;
        try {
            type = VehicleType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FeeSavingResponse.failure("Invalid vehicle type: " + vehicleType);
        }
        
        city = city.toUpperCase();
        Optional<WeatherStation> latestStation = weatherStationRepository.findLatestByStationName(cityNameToStationName(city));

        if (latestStation.isEmpty()) {
            return new FeeSavingResponse(false, "No weather station data available for: " + city + ". It is " +
                    "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU.");
        }
        
        city = city.toUpperCase();
        CityAndVehicleFee fee = cityAndVehicleFeeRepository.findByCityAndVehicleType(city, type)
                .orElseGet(CityAndVehicleFee::new);

        fee.setCity(city);
        fee.setVehicleType(type);
        fee.setAmount(newFee);
        cityAndVehicleFeeRepository.save(fee);

        return FeeSavingResponse.success("Fee successfully updated for " + city + " and vehicle type " + vehicleType + ".");

    }

    private String cityNameToStationName(String cityName) {
        return switch (cityName) {
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
            default -> null;
        };
    }
}
