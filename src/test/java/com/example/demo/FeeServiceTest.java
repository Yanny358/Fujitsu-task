package com.example.demo;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.model.WeatherStation;
import com.example.demo.repository.CityAndVehicleFeeRepository;
import com.example.demo.repository.WeatherStationRepository;
import com.example.demo.service.fee.FeeService;
import com.example.demo.service.fee.FeeWeatherBased;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.service.fee.dto.FeeSavingResponse;
import com.example.demo.utils.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FeeServiceTest {
    
    @Autowired
    private FeeWeatherBased feeWeatherBased;

    @Autowired
    private FeeService feeService;

    @MockBean
    private CityAndVehicleFeeRepository cityAndVehicleFeeRepository;

    @MockBean
    private WeatherStationRepository weatherStationRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(cityAndVehicleFeeRepository, weatherStationRepository);
    }


    @Test
    void unsupportedCity() {
        // Setup
        String city = "VILJANDI";
        VehicleType vehicleType = VehicleType.SCOOTER;
        double airTemperature = 3.1;
        double windSpeed = 7.7;
        String phenomenon = "";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertNotNull(response.getErrorMessage());
        assertEquals("No weather station data available for: " + city + ". It is " +
                "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU.",  response.getErrorMessage()); 
    }

    @Test
    void successfulUpdate() {
        // Given
        String city = "TALLINN";
        String vehicleType = "CAR";
        double newFee = 4.5;
        when(cityAndVehicleFeeRepository.findByCityAndVehicleType(city.toUpperCase(), VehicleType.CAR))
                .thenReturn(Optional.of(new CityAndVehicleFee()));
        when(weatherStationRepository.findLatestByStationName(anyString()))
                .thenReturn(Optional.of(new WeatherStation()));

        // When
        FeeSavingResponse response = feeService.updateBaseFee(city, vehicleType, newFee);

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Fee successfully updated for TALLINN and vehicle type CAR.", response.getMessage());
        verify(cityAndVehicleFeeRepository, times(1)).save(any(CityAndVehicleFee.class));
    }

    @Test
    void updateBaseFee_InvalidVehicleType() {
        // Given
        String city = "TALLINN";
        String invalidVehicleType = "HELICOPTER";
        double newFee = 4.5;

        // When
        FeeSavingResponse response = feeService.updateBaseFee(city, invalidVehicleType, newFee);

        // Then
        assertFalse(response.isSuccess());
        assertEquals("Invalid vehicle type: HELICOPTER", response.getMessage());
    }

    @Test
    void updateBaseFee_UnsupportedCity() {
        // Given
        String unsupportedCity = "NARVA";
        String vehicleType = "CAR";
        double newFee = 4.5;
        when(weatherStationRepository.findLatestByStationName(anyString()))
                .thenReturn(Optional.empty());

        // When
        FeeSavingResponse response = feeService.updateBaseFee(unsupportedCity, vehicleType, newFee);

        // Then
        assertFalse(response.isSuccess());
        assertEquals("No weather station data available for: NARVA. It is " +
                "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU.", response.getMessage());
    }

}
