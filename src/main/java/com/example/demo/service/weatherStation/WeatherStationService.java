package com.example.demo.service.weatherStation;

import com.example.demo.model.WeatherStation;
import com.example.demo.repository.WeatherStationRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherStationService {
    
    private final WeatherStationRepository weatherStationRepository;

    public void fetchAndSaveObservationsForStations(List<String> cities) throws IOException {
        Document doc = fetchWeatherData();
        Instant timestamp = parseTimestamp(doc);
        List<WeatherStation> weatherStations = parseWeatherStations(doc, cities, timestamp);
        saveWeatherStations(weatherStations);
    }

    private Document fetchWeatherData() throws IOException {
        String url = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
        return Jsoup.connect(url).get();
    }

    private Instant parseTimestamp(Document doc) {
        Element observationsElement = doc.selectFirst("observations");
        String timestampString = observationsElement.attr("timestamp");
        return Instant.ofEpochSecond(Long.parseLong(timestampString));
    }

    private List<WeatherStation> parseWeatherStations(Document doc, List<String> cities, Instant timestamp) {
        List<WeatherStation> stations = new ArrayList<>();
        Elements observationElements = doc.select("observations > station");
        for (Element observationElement : observationElements) {
            String name = observationElement.select("name").text();
            if (cities.contains(name)) {
                stations.add(createWeatherStation(observationElement, timestamp));
            }
        }
        return stations;
    }

    private WeatherStation createWeatherStation(Element observationElement, Instant timestamp) {
        String name = observationElement.select("name").text();
        String wmoCode = observationElement.select("wmocode").text();
        double airTemperature = Double.parseDouble(observationElement.select("airtemperature").text());
        double windSpeed = Double.parseDouble(observationElement.select("windspeed").text());
        String weatherPhenomenon = observationElement.select("phenomenon").text();

        WeatherStation station = new WeatherStation();
        station.setName(name);
        station.setWmoCode(wmoCode);
        station.setAirTemperature(airTemperature);
        station.setWindSpeed(windSpeed);
        station.setWeatherPhenomenon(weatherPhenomenon);
        station.setTimeStamp(timestamp.atZone(ZoneId.of("Europe/Tallinn")));
        return station;
    }

    private void saveWeatherStations(List<WeatherStation> stations) {
        stations.forEach(weatherStationRepository::save);
    }
}
