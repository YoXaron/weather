package dev.yoxaron.weather.client.exception;

import lombok.Getter;

@Getter
public class WeatherApiException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;

    public WeatherApiException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
