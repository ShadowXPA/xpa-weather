package xpa.shadow.weather.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xpa.shadow.weather.model.Coordinates;
import xpa.shadow.weather.model.Units;
import xpa.shadow.weather.model.Weather;

import java.util.Optional;

@Log4j2
@Service
public class WeatherService {

    @Autowired
    private GeocodingApiService geocodingApiService;
    @Autowired
    private WeatherApiService weatherApiService;

    public Optional<Weather> getWeather(String geocodingApiKey, String weatherApiKey, String address, Units units) {
        log.info("Getting coordinates...");
        Optional<Coordinates> coordinates = geocodingApiService.getCoordinates(geocodingApiKey, address);

        if (coordinates.isEmpty()) {
            log.warn("No coordinates were found!");
            return Optional.empty();
        }

        log.info("Found coordinates, getting weather and forecast...");
        return weatherApiService.getWeather(weatherApiKey, coordinates.get(), units);
    }
}
