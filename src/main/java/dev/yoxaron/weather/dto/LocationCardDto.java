package dev.yoxaron.weather.dto;

import dev.yoxaron.weather.client.dto.LocationApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationCardDto {

    private String cityName;
    private double latitude;
    private double longitude;
    private String country;
    private String state;

    public static LocationCardDto fromApiResponse(LocationApiResponse apiResponse) {
        return LocationCardDto.builder()
                .cityName(apiResponse.getName())
                .latitude(apiResponse.getLat())
                .longitude(apiResponse.getLon())
                .country(apiResponse.getCountry())
                .state(apiResponse.getState())
                .build();
    }
}
