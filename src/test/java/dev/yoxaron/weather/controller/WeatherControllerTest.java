package dev.yoxaron.weather.controller;

import dev.yoxaron.weather.dto.LocationCardDto;
import dev.yoxaron.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(weatherController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldReturnSearchPageWithLocations() throws Exception {
        //given
        List<LocationCardDto> locations = List.of(
                LocationCardDto.builder()
                        .cityName("New York")
                        .latitude(40.7127281)
                        .longitude(-74.0060152)
                        .country("US")
                        .state("New York")
                        .build(),
                LocationCardDto.builder()
                        .cityName("New York")
                        .latitude(55.0252998)
                        .longitude(-1.4869496)
                        .country("GB")
                        .state("England")
                        .build()
        );

        when(weatherService.searchLocationByName("New York")).thenReturn(locations);

        //when and then
        mockMvc.perform(get("/weather/search").param("location", "New York"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attribute("locations", locations));
    }
}
