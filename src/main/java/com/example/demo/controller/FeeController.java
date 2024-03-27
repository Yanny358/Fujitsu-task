package com.example.demo.controller;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.service.fee.FeeService;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.service.fee.dto.FeeSavingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class FeeController {
    
    private final FeeService feeService;

    /**
     * Calculates the fee based on the specified city and vehicle type, considering current weather conditions.
     * The fee calculation takes into account air temperature, wind speed, and weather phenomena to adjust the base fee dynamically.
     *
     * Example URL: {@code GET /api/calculateFee/Tallinn/CAR}
     *
     * @param city the city for which the fee calculation is requested (e.g., "Tallinn").
     * @param vehicleType the type of vehicle for which the fee is being calculated (e.g., "CAR").
     * @return a {@link ResponseEntity} containing a {@link FeeCalculationResponse} with either the calculated fee or an error message.
     *         Returns bad request status if the input parameters are invalid or if no weather station data is available for the specified city.
     */
    @GetMapping("/calculateFee/{city}/{vehicleType}")
    public ResponseEntity<FeeCalculationResponse> calculateFee(@PathVariable String city, @PathVariable String vehicleType) {
        try {
            FeeCalculationResponse response = feeService.findWeatherStationAndCalculateFee(city, vehicleType);
            if (response.getErrorMessage() != null) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new FeeCalculationResponse(e.getMessage()));
        }
    }

    /**
     * Updates the base fee for a given city and vehicle type combination.
     * This endpoint allows update the base fee used in fee calculations.
     *
     * Example URL: {@code POST /api/setBaseFee/Tallinn/CAR?fee=5.0}
     *
     * @param city the city for which the base fee is being set (e.g., "Tallinn").
     * @param vehicleType the vehicle type for which the base fee is being set (e.g., "CAR").
     * @param fee the new base fee to be set (e.g., 5.0).
     * @return a {@link ResponseEntity} containing a {@link FeeSavingResponse} indicating the success or failure of the operation.
     *         Returns CREATED status if the base fee was successfully updated, or bad request status if the operation fails (e.g., due to invalid input parameters).
     */
    @PostMapping("/setBaseFee/{city}/{vehicleType}")
    public ResponseEntity<FeeSavingResponse> updateBaseFee(@Valid @PathVariable String city, @PathVariable String vehicleType,
                                                           @RequestParam double fee) {
        FeeSavingResponse response = feeService.updateBaseFee(city, vehicleType, fee);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    /**
     * Retrieves a list of all city and vehicle fee configurations from the database.
     * Each item in the list contains details about the city, the type of vehicle, and the corresponding fee amount.
     *
     * <p>Example usage:
     * <pre>{@code
     * GET /api/getCitiesAndFees
     * }</pre>
     *
     * <p>This method does not require any request parameters. It returns a list of {@link CityAndVehicleFee} objects,
     * each representing a fee configuration for a specific city and vehicle type. The response includes the city name,
     * vehicle type, and the fee amount, but excludes the internal database ID for each record.
     *
     * <p>Response example (JSON):
     * <pre>{@code
     * [
     *   {
     *     "city": "Tallinn",
     *     "vehicleType": "CAR",
     *     "amount": 5.0
     *   }
     * ]
     * }</pre>
     *
     * @return a {@link List} of {@link CityAndVehicleFee} objects representing all fee configurations available.
     */
    @GetMapping("/getCitiesAndFees")
    public List<CityAndVehicleFee> getAllFees() {
        return feeService.getAllFees();
    }
}
