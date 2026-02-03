package dev.yoxaron.weather.dto;

import lombok.Data;

@Data
public class LocationDto {

    private String name;
    private double latitude;
    private double longitude;
}
