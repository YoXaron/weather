package dev.yoxaron.weather.dto;

import dev.yoxaron.weather.client.dto.WeatherApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

    private String city;
    private String country;
    private double temperature;
    private double feelsLikeTemperature;
    private String description;
    private int humidity;
    private String icon;
    private double latitude;
    private double longitude;

    public static WeatherDto fromLocationAndWeather(WeatherApiResponse weather) {
        String description = "";
        String icon = "";
        if (weather.getWeather() != null && !weather.getWeather().isEmpty()) {
            WeatherApiResponse.Weather w = weather.getWeather().get(0);
            description = capitalizeFirst(w.getDescription());
            icon = w.getIcon();
        }

        return WeatherDto.builder()
                .city(weather.getName())
                .country(weather.getSys().getCountry())
                .temperature(weather.getMain().getTemp())
                .feelsLikeTemperature(weather.getMain().getFeelsLike())
                .description(description)
                .humidity(weather.getMain().getHumidity())
                .icon(icon)
                .latitude(weather.getCoord().getLat())
                .longitude(weather.getCoord().getLon())
                .build();
    }

    private static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
