package com.example.demo.service.fee;

import com.example.demo.utils.VehicleType;

public class TartuFees implements CityFee{
    @Override
    public double mapFee(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> 3.5;
            case SCOOTER -> 3.0;
            case BIKE -> 2.5;
        };
    }
}
