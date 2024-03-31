package com.example.demo;

import com.example.demo.controller.PhenomenonFeeController;
import com.example.demo.model.PhenomenonExtraFee;
import com.example.demo.service.extraFee.PhenomenonService;
import com.example.demo.validationError.PhenomenonFeeValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhenomenonFeeController.class)
class PhenomenonFeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhenomenonService phenomenonService;

    @Autowired
    private ObjectMapper objectMapper;

    private PhenomenonExtraFee phenomenonExtraFee;

    @BeforeEach
    void setUp() {
        phenomenonExtraFee = new PhenomenonExtraFee();
        phenomenonExtraFee.setId(1L);
        phenomenonExtraFee.setName("Light rain");
        phenomenonExtraFee.setAdjustmentAmount(0.5);
    }

    @Test
    void getAllPhenomenonFees() throws Exception {
        List<PhenomenonExtraFee> allPhenomenonFees = Arrays.asList(phenomenonExtraFee);
        given(phenomenonService.getAllPhenomenonFees()).willReturn(allPhenomenonFees);

        mockMvc.perform(get("/api/phenomenonFees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allPhenomenonFees.size()))
                .andExpect(jsonPath("$[0].name").value(phenomenonExtraFee.getName()));
    }

    @Test
    void updatePhenomenonFee_Success() throws Exception {
        Map<String, Double> updateRequest = new HashMap<>();
        double newAdjustmentAmount = 1.0;
        updateRequest.put("adjustmentAmount", newAdjustmentAmount);

        PhenomenonExtraFee updatedPhenomenonExtraFee = new PhenomenonExtraFee();
        updatedPhenomenonExtraFee.setId(phenomenonExtraFee.getId());
        updatedPhenomenonExtraFee.setName(phenomenonExtraFee.getName());
        updatedPhenomenonExtraFee.setAdjustmentAmount(newAdjustmentAmount);

        given(phenomenonService.updateFee(anyLong(), anyDouble())).willReturn(updatedPhenomenonExtraFee);

        mockMvc.perform(put("/api/phenomenonFees/{id}", phenomenonExtraFee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adjustmentAmount").value(newAdjustmentAmount));
    }
    
    @Test
    void updatePhenomenonFee_NotFound() throws Exception {
        Map<String, Double> updateRequest = new HashMap<>();
        updateRequest.put("adjustmentAmount", 1.0);

        given(phenomenonService.updateFee(anyLong(), anyDouble())).willReturn(null);

        mockMvc.perform(put("/api/phenomenonFees/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePhenomenonFee_BadRequest() throws Exception {
        Map<String, Double> updateRequest = new HashMap<>();
        updateRequest.put("adjustmentAmount", -1.0);
        
        given(phenomenonService.updateFee(anyLong(), anyDouble()))
                .willThrow(new PhenomenonFeeValidationException("Adjustment amount value cannot be less than 0!"));

        mockMvc.perform(put("/api/phenomenonFees/{id}", phenomenonExtraFee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Adjustment amount value cannot be less than 0!")));
    }
}
