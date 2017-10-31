package org.panero.platform.weather;

import org.panero.common.URLBuilder;
import org.panero.platform.weather.detail.WeatherForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;

@Component
@EnableConfigurationProperties(WeatherProperties.class)
public class WeatherForecastTask {
    private static Logger logger = LoggerFactory.getLogger(WeatherForecastTask.class);

    @Autowired
    private WeatherProperties properties;

    @Autowired
    private WeatherService service;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Validator validator;

    @Scheduled(fixedDelayString = "${panero.integration.weather.delay-forecast}000")
    public void weatherForecast() {
        if (properties.getCities() == null) return;
        final URLBuilder baseUrl = URLBuilder.create()
                .http().host("api.openweathermap.org").path("/data/2.5/forecast")
                .queryParam("units", properties.getUnits())
                .queryParam("lang", properties.getLang())
                .queryParam("appid", properties.getApiKey());
        properties.getCities().forEach(city -> {
            final String url = new URLBuilder(baseUrl).queryParam("id", city.getId()).and().toString();
            logger.info("Getting weather forecast for tenant [{}]: {}", city.getTenant(), url);
            final WeatherForecastResponse response = restTemplate.getForObject(url, WeatherForecastResponse.class);
            logger.info("Updating weather forecast for tenant [{}]: {}", city.getTenant(), response);
            if (validator.supports(WeatherForecastResponse.class)) {
                logger.debug("Validating WeatherForecastResponse object...");
                final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(response, response.getClass().getSimpleName());
                validator.validate(response, errors);
                if (!errors.getAllErrors().isEmpty()) {
                    logger.error("Response from weather service is invalid: {}", errors.toString());
                    return;
                }
                logger.debug("Valid WeatherResponse object received");
            }
            service.updateWeatherForecast(city.getTenant(), response);
        });
    }
}
