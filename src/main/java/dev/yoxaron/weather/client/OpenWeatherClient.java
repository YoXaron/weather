package dev.yoxaron.weather.client;

import dev.yoxaron.weather.client.dto.LocationApiResponse;
import dev.yoxaron.weather.client.dto.WeatherApiResponse;
import dev.yoxaron.weather.client.exception.WeatherApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
public class OpenWeatherClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public OpenWeatherClient(
            RestTemplate restTemplate,
            @Value("${openweather.api.base-url}") String baseUrl,
            @Value("${openweather.api.key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Cacheable(value = "locations", key = "#query")
    public List<LocationApiResponse> searchLocations(String query) {
        log.info("Searching locations: query='{}'", query);

        try {
            URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/geo/1.0/direct")
                    .queryParam("q", query)
                    .queryParam("limit", 5)
                    .queryParam("appid", apiKey)
                    .build().toUri();

            log.debug("Request URL: {}", uri);

            ResponseEntity<List<LocationApiResponse>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LocationApiResponse>>() {
                    }
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<LocationApiResponse> locations = response.getBody();
                log.info("Found {} locations for query '{}'", locations.size(), query);
                return locations;
            }

            throw new WeatherApiException("Empty response from API", 500, "EMPTY_RESPONSE");

        } catch (HttpClientErrorException e) {
            log.error("Client error while searching locations: {}", e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new WeatherApiException(
                        "Locations not found for: " + query,
                        404,
                        "NOT_FOUND"
                );
            }
            throw new WeatherApiException(
                    "Error searching locations: " + e.getMessage(),
                    e.getStatusCode().value(),
                    "CLIENT_ERROR"
            );
        } catch (HttpServerErrorException e) {
            log.error("Server error while searching locations: {}", e.getMessage());
            throw new WeatherApiException(
                    "OpenWeather API server error",
                    e.getStatusCode().value(),
                    "SERVER_ERROR"
            );
        } catch (Exception e) {
            log.error("Unexpected error while searching locations: {}", e.getMessage(), e);
            throw new WeatherApiException(
                    "Unexpected error: " + e.getMessage(),
                    500,
                    "INTERNAL_ERROR"
            );
        }
    }

    @Cacheable(value = "weathers", key = "#lat.toString() + '_' + #lon.toString()")
    public WeatherApiResponse getWeatherByCoordinates(double lat, double lon) {
        log.info("Getting weather by coordinates: lat={}, lon={}", lat, lon);

        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(baseUrl + "/data/2.5/weather")
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("units", "metric")
                    .queryParam("appid", apiKey)
                    .build()
                    .toUri();

            log.debug("Request URL: {}", uri);

            ResponseEntity<WeatherApiResponse> response = restTemplate.getForEntity(
                    uri,
                    WeatherApiResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                WeatherApiResponse weather = response.getBody();
                log.info("Successfully retrieved weather for: {}", weather.getName());
                return weather;
            }

            throw new WeatherApiException("Empty response from API", 500, "EMPTY_RESPONSE");

        } catch (HttpClientErrorException e) {
            log.error("Client error while getting weather: {}", e.getMessage());
            throw new WeatherApiException(
                    "Invalid coordinates or parameters",
                    e.getStatusCode().value(),
                    "CLIENT_ERROR"
            );
        } catch (HttpServerErrorException e) {
            log.error("Server error while getting weather: {}", e.getMessage());
            throw new WeatherApiException(
                    "OpenWeather API server error",
                    e.getStatusCode().value(),
                    "SERVER_ERROR"
            );
        } catch (Exception e) {
            log.error("Unexpected error while getting weather: {}", e.getMessage(), e);
            throw new WeatherApiException(
                    "Unexpected error: " + e.getMessage(),
                    500,
                    "INTERNAL_ERROR"
            );
        }
    }
}
