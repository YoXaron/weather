package dev.yoxaron.weather.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Configuration
@PropertySource("classpath:application.properties")
public class RestTemplateConfig {

    @Value("${openweather.api.timeout:5000}")
    private int timeout;

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeout);
        requestFactory.setReadTimeout(timeout);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.removeIf(converter ->
                converter instanceof MappingJackson2HttpMessageConverter);

        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(objectMapper);
        converters.add(jacksonConverter);

        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .findAndAddModules()
                .build();
    }
}
