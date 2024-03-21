package com.example.demo;

import com.example.demo.controller.FeeController;
import com.example.demo.service.fee.FeeService;
import com.example.demo.service.fee.dto.FeeCalculationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        when(feeService.findWeatherStationAndCalculateFee(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid vehicle type: INVALID"));

        this.mockMvc.perform(get("/api/calculateFee/TALLINN/INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid vehicle type: INVALID"));

        verify(feeService).findWeatherStationAndCalculateFee("TALLINN", "INVALID");
    }

    @Test
    void calculateFee_ReturnsErrorForInvalidStation() throws Exception {
        when(feeService.findWeatherStationAndCalculateFee(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("No weather station data available for VILJANDI"));

        this.mockMvc.perform(get("/api/calculateFee/VILJANDI/CAR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(
                        "No weather station data available for VILJANDI"));

        verify(feeService).findWeatherStationAndCalculateFee("VILJANDI", "CAR");
    }
}
