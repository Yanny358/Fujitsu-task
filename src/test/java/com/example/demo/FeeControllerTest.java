package com.example.demo;

import com.example.demo.controller.FeeController;
import com.example.demo.service.fee.FeeService;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import com.example.demo.service.fee.dto.FeeSavingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeeController.class)
public class FeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeeService feeService;
    

    @Test
    void calculateFee_ReturnsFee() throws Exception {
        when(feeService.findWeatherStationAndCalculateFee("TARTU", "BIKE"))
                .thenReturn(new FeeCalculationResponse(2.5));

        this.mockMvc.perform(get("/api/calculateFee/TARTU/BIKE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fee").value(2.5))
                .andExpect(jsonPath("$.errorMessage").doesNotExist());

        verify(feeService).findWeatherStationAndCalculateFee("TARTU", "BIKE");
    }

    @Test
    void calculateFee_ReturnsErrorForInvalidVehicleType() throws Exception {
        when(feeService.findWeatherStationAndCalculateFee("TALLINN", "SKATE"))
                .thenThrow(new IllegalArgumentException("Invalid vehicle type: SKATE"));

        this.mockMvc.perform(get("/api/calculateFee/TALLINN/SKATE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid vehicle type: SKATE"));

        verify(feeService).findWeatherStationAndCalculateFee("TALLINN", "SKATE");
    }

    @Test
    void calculateFee_ReturnsErrorForInvalidStation() throws Exception {
        when(feeService.findWeatherStationAndCalculateFee("VILJANDI", "CAR"))
                .thenThrow(new IllegalArgumentException("No weather station data available for: VILJANDI. It is " +
                        "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU."));

        this.mockMvc.perform(get("/api/calculateFee/VILJANDI/CAR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(
                        "No weather station data available for: VILJANDI. It is " +
                                "not in database yet or not in supported cities: TALLINN, TARTU, PÄRNU."));

        verify(feeService).findWeatherStationAndCalculateFee("VILJANDI", "CAR");
    }

    @Test
    void calculateFee_InvalidVehicleType() throws Exception {
        String invalidVehicleType = "BOAT";
        String city = "TALLINN";
        when(feeService.findWeatherStationAndCalculateFee(city, invalidVehicleType))
                .thenThrow(new IllegalArgumentException("Invalid vehicle type: " + invalidVehicleType));

        mockMvc.perform(get("/api/calculateFee/{city}/{vehicleType}", city, invalidVehicleType))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid vehicle type: " + invalidVehicleType));
    }

    @Test
    void updateBaseFee_Success() throws Exception {
        FeeSavingResponse mockResponse = FeeSavingResponse.success("Fee successfully updated for TALLINN and vehicle type CAR.");
        when(feeService.updateBaseFee("TALLINN", "CAR", 5.0)).thenReturn(mockResponse);

        mockMvc.perform(put("/api/setBaseFee/TALLINN/CAR?fee=5.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Fee successfully updated for TALLINN and vehicle type CAR."));
    }

    @Test
    void updateBaseFee_Failure() throws Exception {
        FeeSavingResponse mockResponse = FeeSavingResponse.failure("Invalid vehicle type: BOAT");
        when(feeService.updateBaseFee("TALLINN", "BOAT", 5.0)).thenReturn(mockResponse);

        mockMvc.perform(put("/api/setBaseFee/TALLINN/BOAT?fee=5.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid vehicle type: BOAT"));
    }


}
