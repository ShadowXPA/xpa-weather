package xpa.shadow.weather.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import xpa.shadow.weather.model.Coordinates;
import xpa.shadow.weather.model.OpenWeatherMapResponse;
import xpa.shadow.weather.model.Units;
import xpa.shadow.weather.model.Weather;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Weather service from <a href="https://openweathermap.org/">OpenWeatherMap</a>
 */
@Log4j2
@Service("OpenWeatherMap")
public class OpenWeatherMapApiService implements WeatherApiService {

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private Gson gson;

    @Override
    public Optional<Weather> getWeather(String apiKey, Coordinates coordinates, Units units) {
        log.info("Getting weather and forecast for coordinates: {}...", coordinates);
        log.debug("Using API Key: {}", apiKey);

        HttpGet weatherRequest = new HttpGet("https://api.openweathermap.org/data/2.5/weather?"
                + "appid=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8)
                + "&lat=" + coordinates.getLatitude()
                + "&lon=" + coordinates.getLongitude()
                + "&units=" + units.name().toLowerCase());

        try (CloseableHttpResponse weatherResponse = httpClient.execute(weatherRequest)) {
            HttpStatus weatherHttpStatus = HttpStatus.resolve(weatherResponse.getStatusLine().getStatusCode());
            log.info("Got weather response code: {}", weatherHttpStatus);
            if (weatherHttpStatus == HttpStatus.OK) {
                String weatherResponseString = EntityUtils.toString(weatherResponse.getEntity());
                OpenWeatherMapResponse openWeatherMapResponse = gson.fromJson(weatherResponseString,
                        OpenWeatherMapResponse.class);
                Weather weather = openWeatherMapResponse.toWeather();

                weather.setCoordinates(coordinates);
                return Optional.of(weather);
            }
        } catch (Exception ex) {
            log.error("An error occurred while trying to call the API", ex);
        }

        return Optional.empty();
    }
}
