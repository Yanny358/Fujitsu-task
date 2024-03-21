package com.example.demo;

import com.example.demo.service.fee.FeeWeatherBased;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.utils.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FeeServiceTest {
    
    @Autowired
    private FeeWeatherBased feeWeatherBased;
    
    @Test
    void bikeInTartuNormalConditions() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = 20.0;
        double windSpeed = 5.0;
        String phenomenon = "";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(2.5), response.getFee()); 
    }

    @Test
    void bikeInTartuSnowColdTemp() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = -2.1;
        double windSpeed = 4.7;
        String phenomenon = "Light snow shower";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(4.0), response.getFee());
    }

    @Test
    void bikeInTartuThunder() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = -2.1;
        double windSpeed = 4.7;
        String phenomenon = "Thunder";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertNotNull(response.getErrorMessage());
        assertEquals("Usage of selected vehicle type is forbidden", response.getErrorMessage());
    }

    @Test
    void bikeInTartuStrongWind() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = 3.1;
        double windSpeed = 20.7;
        String phenomenon = "";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertNotNull(response.getErrorMessage());
        assertEquals("Usage of selected vehicle type is forbidden", response.getErrorMessage());
    }

    @Test
    void scooterInTallinnRain() {
        // Setup
        String city = "TALLINN";
        VehicleType vehicleType = VehicleType.SCOOTER;
        double airTemperature = 3.1;
        double windSpeed = 7.7;
        String phenomenon = "Light rain";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(4.0), response.getFee());
    }
    @Test
    void scooterInTallinnSnowShower() {
        // Setup
        String city = "TALLINN";
        VehicleType vehicleType = VehicleType.SCOOTER;
        double airTemperature = 3.1;
        double windSpeed = 7.7;
        String phenomenon = "Light snow shower";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(4.5), response.getFee());
    }

    @Test
    void bikeInPärnuCold() {
        // Setup
        String city = "PÄRNU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = -13.1;
        double windSpeed = 7.7;
        String phenomenon = "";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(3.0), response.getFee());
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
        assertEquals("Unsupported city: " + city,  response.getErrorMessage());
    }
}
