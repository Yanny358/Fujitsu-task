package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Data
public class WeatherStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    public String wmoCode;

    public double airTemperature;

    public double windSpeed;

    public String weatherPhenomenon;
    
    public ZonedDateTime timeStamp;
    
}
