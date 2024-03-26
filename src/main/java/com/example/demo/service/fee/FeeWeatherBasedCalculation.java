package com.example.demo.service.fee;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.repository.CityAndVehicleFeeRepository;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.utils.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Component responsible for calculating fees based on weather conditions and predefined city and vehicle type fees.
 * It utilizes weather data and city-vehicle fee mappings to adjust fees accordingly.
 */
@Component
@RequiredArgsConstructor
public class FeeWeatherBasedCalculation implements FeeWeatherBased {

    private final CityAndVehicleFeeRepository cityAndVehicleFeeRepository;

    /**
     * Calculates the fee based on air temperature, wind speed, weather phenomenon, city, and vehicle type.
     * If the fee for the specified city and vehicle type is not found, it returns an error message.
     * Otherwise, it adjusts the fee based on weather conditions.
     *
     * @param airTemperature the current air temperature
     * @param windSpeed the current wind speed
     * @param phenomenon the current weather phenomenon
     * @param city the city for which the fee is being calculated
     * @param vehicleType the type of vehicle for which the fee is being calculated
     * @return a {@link FeeCalculationResponse} containing the calculated fee or an error message
     */
    @Override
    public FeeCalculationResponse calculate(double airTemperature, double windSpeed, String phenomenon, String city,
                                            VehicleType vehicleType) {

        city = city.toUpperCase();
        Optional<CityAndVehicleFee> feeOptional = cityAndVehicleFeeRepository.findByCityAndVehicleType(city, vehicleType);

        if (feeOptional.isEmpty()) {
            return new FeeCalculationResponse("No weather station data available for: " + city + ". It is " +
                    "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU.");
        }
        double baseFee = feeOptional.get().getAmount();

        FeeCalculationResponse weatherAdjustmentResponse = calculateWeatherAdjustments(airTemperature, windSpeed, phenomenon, vehicleType);

        if (weatherAdjustmentResponse.getErrorMessage() != null) {
            return weatherAdjustmentResponse;
        }
        double adjustedFee = baseFee + weatherAdjustmentResponse.getFee();

        return new FeeCalculationResponse(adjustedFee);
    }

    private FeeCalculationResponse calculateWeatherAdjustments(double airTemperature, double windSpeed, String phenomenon, VehicleType vehicleType) {
        double adjustment = 0;
        if (vehicleType == VehicleType.BIKE) {
            if (windSpeed >= 10.0 && windSpeed <= 20.0) adjustment += 0.5;
            if (windSpeed > 20.0) {
                return new FeeCalculationResponse("Usage of selected vehicle type is forbidden");
            }
        }
        if (vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) {
            if (airTemperature < -10.0) adjustment += 1.0;
            if (airTemperature >= -10.0 && airTemperature <= 0) adjustment += 0.5;

            boolean hasSnow = phenomenon.contains("snow");
            boolean hasSleet = phenomenon.contains("sleet");
            boolean hasRain = phenomenon.contains("rain");
            // Only consider shower if snow hasn't been detected. Real cases: 'Light snow shower' 'Light shower'
            boolean hasShower = phenomenon.contains("shower") && !hasSnow;
            if (hasSnow || hasSleet) adjustment += 1;
            if (hasRain || hasShower) adjustment += 0.5;
            if (phenomenon.contains("Glaze") || phenomenon.contains("Hail") || phenomenon.contains("Thunder")) {
                return new FeeCalculationResponse("Usage of selected vehicle type is forbidden");
            }
        }
        return new FeeCalculationResponse(adjustment);
    }

    /**
     * Populates the database with default fees for all supported city and vehicle type combinations.
     * This method is intended to be called during application initialization to ensure that the database contains all necessary default fee data.
     */
    public void saveDefaultFees() {
        Map<String, Map<VehicleType, Double>> defaultFees = getDefaultFees();

        defaultFees.forEach((city, vehicleTypeMap) -> vehicleTypeMap.forEach((vehicleType, fee) -> {
            CityAndVehicleFee cityAndVehicleFee = new CityAndVehicleFee();
            cityAndVehicleFee.setCity(city);
            cityAndVehicleFee.setVehicleType(vehicleType);
            cityAndVehicleFee.setAmount(fee);
            cityAndVehicleFeeRepository.save(cityAndVehicleFee);
        }));
    }

    private Map<String, Map<VehicleType, Double>> getDefaultFees() {
        Map<String, Map<VehicleType, Double>> defaultFees = new HashMap<>();

        defaultFees.put("TALLINN", Map.of(
                VehicleType.CAR, 4.0,
                VehicleType.SCOOTER, 3.5,
                VehicleType.BIKE, 3.0));
        defaultFees.put("TARTU", Map.of(
                VehicleType.CAR, 3.5,
                VehicleType.SCOOTER, 3.0,
                VehicleType.BIKE, 2.5));
        defaultFees.put("PÄRNU", Map.of(
                VehicleType.CAR, 3.0,
                VehicleType.SCOOTER, 2.5,
                VehicleType.BIKE, 2.0));

        return defaultFees;
    }

}
