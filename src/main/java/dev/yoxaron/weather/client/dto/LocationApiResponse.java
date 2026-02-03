package dev.yoxaron.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationApiResponse {

    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
