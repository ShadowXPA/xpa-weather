package xpa.shadow.weather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * OWM = OpenWeatherMap
 */
public class OWMCurrentWeatherResponse {

    private List<Weather> weather;
    private Main main;
    private int visibility;
    private Wind wind;
    private Cloud clouds;
    private long dt;
    private Sys sys;
    private long timezone;
    private String name;

    public xpa.shadow.weather.model.Weather toWeather() {
        Weather weather = this.weather.get(0);

        return xpa.shadow.weather.model.Weather.builder()
                .location(name)
                .condition(weather.main)
                .description(weather.description)
                .icon(weather.icon)
                .temperature(main.temp)
                .feelsLike(main.feelsLike)
                .pressureSeaLevel(main.seaLevel)
                .pressureGroundLevel(main.grndLevel)
                .humidity(main.humidity)
                .visibility(visibility)
                .windSpeed(wind.speed)
                .windDirection(wind.deg)
                .cloudiness(clouds.all)
                .datetime(dt)
                .countryCode(sys.country)
                .sunrise(sys.sunrise)
                .sunset(sys.sunset)
                .timezone(timezone)
                .build();
    }

    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }

    public static class Main {
        private float temp;
        @SerializedName("feels_like")
        private float feelsLike;
        @SerializedName("sea_level")
        private int seaLevel;
        @SerializedName("grnd_level")
        private int grndLevel;
        private int humidity;
    }

    public static class Wind {
        private float speed;
        private int deg;
    }

    public static class Cloud {
        private int all;
    }

    public static class Sys {
        private String country;
        private long sunrise;
        private long sunset;
    }
}
