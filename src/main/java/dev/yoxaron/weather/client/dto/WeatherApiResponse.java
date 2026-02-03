package dev.yoxaron.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponse {

    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Sys sys;
    private String name;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        private double lat;
        private double lon;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        private int humidity;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        private String country;
    }
}
