package com.example.demo.model;

import com.example.demo.utils.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CityAndVehicleFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    
    private double amount;
    
}
