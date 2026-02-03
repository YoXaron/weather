package dev.yoxaron.weather.controller;

import dev.yoxaron.weather.dto.WeatherDto;
import dev.yoxaron.weather.service.WeatherService;
import dev.yoxaron.weather.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/search")
    public String getLocations(@RequestParam("location") String location, Model model) {
        log.info("Weather Controller getLocations: {}", location);
        model.addAttribute("locations", weatherService.searchLocationByName(location));
        return "search";
    }

    @GetMapping
    public String getWeatherCardsForCurrentUser(Model model, HttpServletRequest request) {
        long currentUserId = CookieUtil.getCurrentUserIdFromRequest(request);
        List<WeatherDto> allWeatherByUserId = weatherService.getAllWeatherByUserId(currentUserId);

        model.addAttribute("weatherList",  allWeatherByUserId);

        return "index";
    }
}
