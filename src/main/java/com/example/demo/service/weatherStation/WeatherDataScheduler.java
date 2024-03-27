package com.example.demo.service.weatherStation;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class WeatherDataScheduler {
    
    private WeatherStationParseAndSave weatherStationParseAndSave;
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataScheduler.class);

    @Scheduled(cron = "${interval-in-cron}") 
    public void fetchWeatherData() {
        List<String> targetStations = Arrays.asList("Pärnu", "Tallinn-Harku", "Tartu-Tõravere");
        try {
            weatherStationParseAndSave.fetchAndSaveObservationsForStations(targetStations);
        } catch (IOException e) {
            logger.error("Failed to fetch and save weather data", e);
        }
    }
}
