package dev.yoxaron.weather.controller;

import dev.yoxaron.weather.dto.WeatherDto;
import dev.yoxaron.weather.service.WeatherService;
import dev.yoxaron.weather.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final WeatherService weatherService;
    private final CacheManager cacheManager;

    @GetMapping("/home")
    public String showHomePage(HttpServletRequest request, Model model) {
        model.addAttribute("username", request.getAttribute("username"));

        long currentUserId = CookieUtil.getCurrentUserIdFromRequest(request);
        List<WeatherDto> allWeatherByUserId = weatherService.getAllWeatherByUserId(currentUserId);

        model.addAttribute("weatherList",  allWeatherByUserId);

        logCacheStats("weathers");
        logCacheStats("locations");

        return "index";
    }

    public void logCacheStats(String cacheName) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (cache != null) {
            log.info("Cache [{}] Stats: {}", cacheName, cache.getNativeCache().stats());
        }
    }
}
