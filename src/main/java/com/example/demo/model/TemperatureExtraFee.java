package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class TemperatureExtraFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = -50, message = "Minimum value for temperature should be -50")
    private Double minValue;

    @Max(value = 50, message = "Maximum value for temperature should be 50")
    private Double maxValue;

    @Min(value = 0, message = "Minimum value for fee should be 0")
    private double adjustmentAmount;
}
