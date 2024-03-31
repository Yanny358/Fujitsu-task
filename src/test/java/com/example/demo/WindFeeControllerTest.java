package com.example.demo;

import com.example.demo.controller.WindFeeController;
import com.example.demo.model.WindExtraFee;
import com.example.demo.service.extraFee.WindFeeService;
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

@WebMvcTest(WindFeeController.class)
class WindFeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WindFeeService windFeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private WindExtraFee windExtraFee;

    @BeforeEach
    void setUp() {
        windExtraFee = new WindExtraFee();
        windExtraFee.setId(1L);
        windExtraFee.setMinValue(5.0);
        windExtraFee.setMaxValue(15.0);
        windExtraFee.setAdjustmentAmount(0.5);
    }

    @Test
    void getAllWindFees() throws Exception {
        List<WindExtraFee> allWindFees = Arrays.asList(windExtraFee);
        given(windFeeService.getAllWindFees()).willReturn(allWindFees);

        mockMvc.perform(get("/api/windFees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allWindFees.size()))
                .andExpect(jsonPath("$[0].minValue").value(windExtraFee.getMinValue()));
    }

    @Test
    void createWindFee() throws Exception {
        given(windFeeService.createWindFee(windExtraFee)).willReturn(windExtraFee);

        mockMvc.perform(post("/api/windFees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(windExtraFee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.minValue").value(windExtraFee.getMinValue()));
    }

    @Test
    void updateWindFee() throws Exception {
        given(windFeeService.updateWindFee(windExtraFee.getId(), windExtraFee)).willReturn(windExtraFee);

        mockMvc.perform(put("/api/windFees/{id}", windExtraFee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(windExtraFee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minValue").value(windExtraFee.getMinValue()));
    }

    @Test
    void deleteWindFee() throws Exception {
        given(windFeeService.deleteWindFee(windExtraFee.getId())).willReturn(true);

        mockMvc.perform(delete("/api/windFees/{id}", windExtraFee.getId()))
                .andExpect(status().isOk());
    }
}
