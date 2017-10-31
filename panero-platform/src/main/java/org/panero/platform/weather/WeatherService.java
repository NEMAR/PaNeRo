package org.panero.platform.weather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.panero.common.model.BatchMeasurements;
import org.panero.common.model.Measurement;
import org.panero.platform.weather.detail.WeatherForecastResponse;
import org.panero.platform.weather.detail.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    public static final String CURRENT_TEMPERATURE = "weather.temperature";
    public static final String CURRENT_PRESSURE = "weather.pressure";
    public static final String CURRENT_HUMIDITY = "weather.humidity";
    public static final String CURRENT_WINDSPEED = "weather.windspeed";
    public static final String CURRENT_CLOUDINESS = "weather.cloudiness";
    public static final String CURRENT_RAIN_VOLUME = "weather.rain_volume";
    public static final String CURRENT_SNOW_VOLUME = "weather.snow_volume";

    public static final String FORECAST_TEMPERATURE = "weather.forecast.temperature";
    public static final String FORECAST_PRESSURE = "weather.forecast.pressure";
    public static final String FORECAST_HUMIDITY = "weather.forecast.humidity";
    public static final String FORECAST_WINDSPEED = "weather.forecast.windspeed";
    public static final String FORECAST_CLOUDINESS = "weather.forecast.cloudiness";
    public static final String FORECAST_RAIN_VOLUME = "weather.forecast.rain_volume";
    public static final String FORECAST_SNOW_VOLUME = "weather.forecast.snow_volume";

    @Autowired
    private WeatherFlowConfiguration.WeatherGateway gateway;

    public void updateCurrentWeather(final String tenant, final WeatherResponse response) {
        final Integer cityId = response.getId();
        final String cityName = response.getName();
        final Double temperature = response.getMain().getTemp();
        final Double pressure = response.getMain().getPressure();
        final Double humidity = response.getMain().getHumidity();
        final Double windspeed = response.getWind().getSpeed();
        final Double cloudiness = response.getClouds().getCloudiness();
        final Double rainVolume = response.getRain().getVolume();
        final Double snowVolume = response.getSnow().getVolume();
        BatchMeasurements.create(tenant)
                .tag("city_id", cityId)
                .tag("city_name", cityName)
                .measurement(Measurement.create(CURRENT_TEMPERATURE)
                        .value(temperature)
                        .and().buildIt())
                .measurement(Measurement.create(CURRENT_PRESSURE)
                        .value(pressure)
                        .and().buildIt())
                .measurement(Measurement.create(CURRENT_HUMIDITY)
                        .value(humidity)
                        .and().buildIt())
                .measurement(Measurement.create(CURRENT_WINDSPEED)
                        .value(windspeed)
                        .and().buildIt())
                .measurement(Measurement.create(CURRENT_CLOUDINESS)
                        .value(cloudiness)
                        .and().buildIt())
                .measurement(Measurement.create(CURRENT_RAIN_VOLUME)
                        .value(rainVolume)
                        .and().buildIt())
                .measurement(Measurement.create(CURRENT_SNOW_VOLUME)
                        .value(snowVolume)
                        .and().buildIt())
                .and().consumeIt(gateway::send);
    }

    public void updateWeatherForecast(final String tenant, final WeatherForecastResponse response) {
        final BatchMeasurements.Builder builder = BatchMeasurements.create(tenant)
                .tag("city_id", response.getCity().getId())
                .tag("city_name", response.getCity().getName())
                .tag("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.getForecast().forEach(forecast -> {
            final Double temperature = forecast.getMain() == null ? 0.0 : forecast.getMain().getTemp();
            final Double pressure = forecast.getMain() == null ? 0.0 : forecast.getMain().getPressure();
            final Double humidity = forecast.getMain() == null ? 0.0 : forecast.getMain().getHumidity();
            final Double windspeed = forecast.getWind() == null ? 0.0 : forecast.getWind().getSpeed();
            final Double cloudiness = forecast.getClouds() == null ? 0.0 : forecast.getClouds().getCloudiness();
            final Double rainVolume = forecast.getRain() == null ? 0.0 : forecast.getRain().getVolume();
            final Double snowVolume = forecast.getSnow() == null ? 0.0 : forecast.getSnow().getVolume();
            builder
                    .measurement(Measurement.create(FORECAST_TEMPERATURE)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(temperature)
                            .and().buildIt())
                    .measurement(Measurement.create(FORECAST_PRESSURE)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(pressure)
                            .and().buildIt())
                    .measurement(Measurement.create(FORECAST_HUMIDITY)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(humidity)
                            .and().buildIt())
                    .measurement(Measurement.create(FORECAST_WINDSPEED)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(windspeed)
                            .and().buildIt())
                    .measurement(Measurement.create(FORECAST_CLOUDINESS)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(cloudiness)
                            .and().buildIt())
                    .measurement(Measurement.create(FORECAST_RAIN_VOLUME)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(rainVolume)
                            .and().buildIt())
                    .measurement(Measurement.create(FORECAST_SNOW_VOLUME)
                            .time(forecast.getDatetime(), TimeUnit.SECONDS)
                            .value(snowVolume)
                            .and().buildIt());
        });
        builder.and().consumeIt(gateway::send);
    }
}
