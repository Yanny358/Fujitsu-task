package com.example.demo.repository;

import com.example.demo.model.WeatherStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherStationRepository extends JpaRepository<WeatherStation, Long> {
    @Query(value = "SELECT * FROM weather_station WHERE name = :name ORDER BY time_stamp DESC LIMIT 1", nativeQuery = true)
    Optional<WeatherStation> findLatestByStationName(@Param("name") String name);
}
