package com.example.demo.service.fee;

import com.example.demo.utils.VehicleType;

public class ParnuFees implements CityFee{
    @Override
    public double mapFee(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> 3.0;
            case SCOOTER -> 2.5;
            case BIKE -> 2.0;
        };
    }
}
