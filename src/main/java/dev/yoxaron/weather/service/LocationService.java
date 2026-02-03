package dev.yoxaron.weather.service;

import dev.yoxaron.weather.dto.LocationDto;
import dev.yoxaron.weather.mapper.LocationMapper;
import dev.yoxaron.weather.model.Location;
import dev.yoxaron.weather.model.User;
import dev.yoxaron.weather.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserService userService;
    private final LocationMapper locationMapper;

    public List<LocationDto> getUserLocations(User user) {
        log.info("Getting locations for user {}", user.getLogin());

        List<Location> locations = locationRepository.findAllByUser(user);
        return locations.stream()
                .map(locationMapper::toLocationDto)
                .toList();
    }

    @Transactional
    public void addLocation(LocationDto locationDto, Long userId) {
        log.info("Adding location {} for User with id {}", locationDto, userId);

        Location location = locationMapper.toLocation(locationDto);
        location.setUser(userService.findById(userId));
        locationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(LocationDto locationDto, Long userId) {
        log.info("Deleting location {} for User with id {}", locationDto, userId);

        locationRepository.deleteByUserUsernameAndCoordinates(
                userId,
                BigDecimal.valueOf(locationDto.getLatitude()),
                BigDecimal.valueOf(locationDto.getLongitude())
        );
    }

}
