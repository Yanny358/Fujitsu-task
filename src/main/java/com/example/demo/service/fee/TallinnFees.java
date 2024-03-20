package com.example.demo.service.fee;

import com.example.demo.utils.VehicleType;

public class TallinnFees implements CityFee{
    @Override
    public double mapFee(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> 4.0;
            case SCOOTER -> 3.5;
            case BIKE -> 3.0;
        };
    }
}
