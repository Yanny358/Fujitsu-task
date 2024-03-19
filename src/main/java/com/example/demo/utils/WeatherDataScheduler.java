package com.example.demo.utils;

import com.example.demo.service.WeatherStationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class WeatherDataScheduler {
    
    private WeatherStationService weatherStationService;

    @Scheduled(cron = "0 15 * * * *") 
    public void fetchWeatherData() {
        List<String> targetCities = Arrays.asList("Pärnu", "Tallinn-Harku", "Tartu-Tõravere");
        try {
            weatherStationService.fetchAndSaveObservationsForCities(targetCities);
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}
