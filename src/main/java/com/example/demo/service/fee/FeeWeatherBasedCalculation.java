package com.example.demo.service.fee;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.model.PhenomenonExtraFee;
import com.example.demo.model.TemperatureExtraFee;
import com.example.demo.model.WindExtraFee;
import com.example.demo.repository.CityAndVehicleFeeRepository;
import com.example.demo.repository.PhenomenonExtraFeeRepository;
import com.example.demo.repository.TemperatureExtraFeeRepository;
import com.example.demo.repository.WindExtraFeeRepository;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.model.enums.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Component responsible for calculating fees based on weather conditions and predefined city and vehicle type fees.
 * It utilizes weather data and city-vehicle fee mappings to adjust fees accordingly.
 */
@Component
@RequiredArgsConstructor
public class FeeWeatherBasedCalculation implements FeeWeatherBased {

    private final CityAndVehicleFeeRepository cityAndVehicleFeeRepository;
    private final PhenomenonExtraFeeRepository phenomenonExtraFeeRepository;
    private final TemperatureExtraFeeRepository temperatureExtraFeeRepository;
    private final WindExtraFeeRepository windExtraFeeRepository;


    /**
     * Calculates the fee based on air temperature, wind speed, weather phenomenon, city, and vehicle type.
     * If the fee for the specified city and vehicle type is not found, it returns an error message.
     * Otherwise, it adjusts the fee based on weather conditions.
     *
     * @param airTemperature the current air temperature
     * @param windSpeed      the current wind speed
     * @param phenomenon     the current weather phenomenon
     * @param city           the city for which the fee is being calculated
     * @param vehicleType    the type of vehicle for which the fee is being calculated
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

        List<TemperatureExtraFee> temperatureExtraFees = temperatureExtraFeeRepository.findAll();
        List<WindExtraFee> windExtraFees = windExtraFeeRepository.findAll();
        List<PhenomenonExtraFee> phenomenonExtraFees = phenomenonExtraFeeRepository.findAll();

        for (TemperatureExtraFee extraFee : temperatureExtraFees) {
            if ((vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) &&
                    airTemperature >= extraFee.getMinValue() && airTemperature <= extraFee.getMaxValue()) {
                adjustment += extraFee.getAdjustmentAmount();
            }
        }
        
        for (WindExtraFee extraFee : windExtraFees) {
            if (vehicleType == VehicleType.BIKE) {
                if (windSpeed >= extraFee.getMinValue() && windSpeed <= extraFee.getMaxValue()) {
                    adjustment += extraFee.getAdjustmentAmount();
                } else if (windSpeed > extraFee.getMaxValue()) {
                    return new FeeCalculationResponse("Usage of selected vehicle type is forbidden");
                }
            }
        }

        for (PhenomenonExtraFee extraFee : phenomenonExtraFees) {
            if ((vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) &&
                    phenomenon.equalsIgnoreCase(extraFee.getName())) {
                adjustment += extraFee.getAdjustmentAmount();
            }
        }

        if ((vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) &&
                (phenomenon.contains("Glaze") || phenomenon.contains("Hail") || phenomenon.contains("Thunder"))) {
            return new FeeCalculationResponse("Usage of selected vehicle type is forbidden");
        }

        return new FeeCalculationResponse(adjustment);
    }

}
    
