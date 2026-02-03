package dev.yoxaron.weather.mapper;

import dev.yoxaron.weather.dto.LocationDto;
import dev.yoxaron.weather.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    Location toLocation(LocationDto locationDto);

    LocationDto toLocationDto(Location location);

    default BigDecimal map(double value) {
        return BigDecimal.valueOf(value);
    }
}
