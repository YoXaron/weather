package dev.yoxaron.weather.service;

import dev.yoxaron.weather.client.OpenWeatherClient;
import dev.yoxaron.weather.client.dto.LocationApiResponse;
import dev.yoxaron.weather.client.dto.WeatherApiResponse;
import dev.yoxaron.weather.dto.LocationCardDto;
import dev.yoxaron.weather.dto.LocationDto;
import dev.yoxaron.weather.dto.WeatherDto;
import dev.yoxaron.weather.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final LocationService locationService;
    private final UserService userService;

    public List<LocationCardDto> searchLocationByName(String locationName) {
        if (locationName == null || locationName.isEmpty()) {
            throw new IllegalArgumentException("locationName cannot be null or empty");
        }

        log.info("Searching for location with name {}", locationName);

        List<LocationApiResponse> locationsResp = openWeatherClient.searchLocations(locationName.trim());

        return locationsResp.stream()
                .map(LocationCardDto::fromApiResponse)
                .toList();
    }

    public List<WeatherDto> getAllWeatherByUserId(long userId) {
        log.info("Fetching all weather for user {}", userId);

        User user = userService.findById(userId);
        List<LocationDto> userLocations = locationService.getUserLocations(user);

        return userLocations.stream()
                .map(loc -> this.getWeatherByCoordinates(
                        loc.getLatitude(),
                        loc.getLongitude()
                ))
                .toList();
    }

    public WeatherDto getWeatherByCoordinates(double lat, double lon) {
        log.info("Getting weather by coordinates {}, {}", lat, lon);

        WeatherApiResponse weatherApiResponse = openWeatherClient.getWeatherByCoordinates(lat, lon);
        return WeatherDto.fromLocationAndWeather(weatherApiResponse);
    }
}
