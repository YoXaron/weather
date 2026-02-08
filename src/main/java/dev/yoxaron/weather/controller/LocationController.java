package dev.yoxaron.weather.controller;

import dev.yoxaron.weather.dto.LocationDto;
import dev.yoxaron.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public String addLocation(@ModelAttribute LocationDto locationDto, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("currentUserId");
        locationService.addLocation(locationDto, currentUserId);
        return "redirect:/home";
    }

    @PostMapping("/delete")
    public String deleteLocation(@ModelAttribute LocationDto locationDto, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("currentUserId");
        locationService.deleteLocation(locationDto, currentUserId);
        return "redirect:/home";
    }
}
