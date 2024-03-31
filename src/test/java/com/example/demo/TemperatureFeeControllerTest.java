package com.example.demo;

import com.example.demo.controller.TemperatureFeeController;
import com.example.demo.model.TemperatureExtraFee;
import com.example.demo.service.extraFee.TemperatureFeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TemperatureFeeController.class)
class TemperatureFeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemperatureFeeService temperatureFeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private TemperatureExtraFee temperatureExtraFee;

    @BeforeEach
    void setUp() {
        temperatureExtraFee = new TemperatureExtraFee();
        temperatureExtraFee.setId(1L);
        temperatureExtraFee.setMinValue(-10.0);
        temperatureExtraFee.setMaxValue(5.0);
        temperatureExtraFee.setAdjustmentAmount(0.5);
    }

    @Test
    void getAllTemperatureFees() throws Exception {
        List<TemperatureExtraFee> allTemperatureFees = Arrays.asList(temperatureExtraFee);
        given(temperatureFeeService.getAllTemperatureFees()).willReturn(allTemperatureFees);

        mockMvc.perform(get("/api/temperatureFees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allTemperatureFees.size()))
                .andExpect(jsonPath("$[0].minValue").value(temperatureExtraFee.getMinValue()));
    }

    @Test
    void createTemperatureFee() throws Exception {
        given(temperatureFeeService.createTemperatureFee(temperatureExtraFee)).willReturn(temperatureExtraFee);

        mockMvc.perform(post("/api/temperatureFees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(temperatureExtraFee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.minValue").value(temperatureExtraFee.getMinValue()));
    }

    @Test
    void updateTemperatureFee() throws Exception {
        given(temperatureFeeService.updateTemperatureFee(temperatureExtraFee.getId(), temperatureExtraFee)).willReturn(temperatureExtraFee);

        mockMvc.perform(put("/api/temperatureFees/{id}", temperatureExtraFee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(temperatureExtraFee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minValue").value(temperatureExtraFee.getMinValue()));
    }

    @Test
    void deleteTemperatureFee() throws Exception {
        given(temperatureFeeService.deleteTemperatureFee(temperatureExtraFee.getId())).willReturn(true);

        mockMvc.perform(delete("/api/temperatureFees/{id}", temperatureExtraFee.getId()))
                .andExpect(status().isOk());
    }
}
