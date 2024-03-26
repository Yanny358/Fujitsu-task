package com.example.demo;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.repository.CityAndVehicleFeeRepository;
import com.example.demo.service.fee.FeeWeatherBased;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.utils.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FeeCalculationsTest {
    @Autowired
    private FeeWeatherBased feeWeatherBased;
    @MockBean
    private CityAndVehicleFeeRepository cityAndVehicleFeeRepository;

    @BeforeEach
    void setUp() {
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;

        CityAndVehicleFee mockedFee = new CityAndVehicleFee();
        mockedFee.setCity(city);
        mockedFee.setVehicleType(vehicleType);
        mockedFee.setAmount(2.5);

        Mockito.when(cityAndVehicleFeeRepository.findByCityAndVehicleType(city, vehicleType))
                .thenReturn(Optional.of(mockedFee));
    }

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
    void bikeInTartuModerateRain() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = 20.0;
        double windSpeed = 5.0;
        String phenomenon = "Moderate rain";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(3.0), response.getFee());
    }

    @Test
    void bikeInTartuModerateShower() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = 20.0;
        double windSpeed = 5.0;
        String phenomenon = "Moderate shower";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(3.0), response.getFee());
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
    void bikeInTartuSnowPositiveTemp() {
        // Setup
        String city = "TARTU";
        VehicleType vehicleType = VehicleType.BIKE;
        double airTemperature = 1.1;
        double windSpeed = 4.7;
        String phenomenon = "Light snow shower";

        // Act
        FeeCalculationResponse response = feeWeatherBased.calculate(
                airTemperature, windSpeed, phenomenon, city, vehicleType);

        // Assert
        assertEquals(Double.valueOf(3.5), response.getFee());
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
}
