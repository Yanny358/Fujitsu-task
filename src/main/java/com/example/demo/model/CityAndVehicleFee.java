package com.example.demo.model;

import com.example.demo.model.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class CityAndVehicleFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String city;
    
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    
    @Min(value = 1, message = "Amount must be minimum 1")
    private double amount;
    
}
