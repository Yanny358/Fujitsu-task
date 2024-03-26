package com.example.demo.repository;

import com.example.demo.model.CityAndVehicleFee;
import com.example.demo.utils.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityAndVehicleFeeRepository extends JpaRepository<CityAndVehicleFee, Long> {
    Optional<CityAndVehicleFee> findByCityAndVehicleType(String city, VehicleType vehicleType);

}
