package org.panero.platform.weather.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.panero.common.Consts;

/**
 * Units:
 * <ul>
 * <li>Temperature in Kelvin, Celsius or Fahrenheit
 * <li>Pressure in hPa
 * <li>Humidity in %
 * <li>Wind speed in meter/sec
 * <li>Cloudiness in %
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private Integer id;

    private String name;

    @NotNull
    @JsonProperty("dt")
    private Long datetime;

    @JsonProperty("dt_txt")
    private String datetimeText;

    @Valid
    private List<Weather> weather = Lists.newArrayList();

    @Valid
    private Main main = new Main();

    @Valid
    private Wind wind = new Wind();

    @Valid
    private Clouds clouds = new Clouds();

    @Valid
    private LastThreeHours rain = new LastThreeHours();

    @Valid
    private LastThreeHours snow = new LastThreeHours();

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(final Long datetime) {
        this.datetime = datetime;
    }

    public String getDatetimeText() {
        return datetimeText;
    }

    public void setDatetimeText(final String datetimeText) {
        this.datetimeText = datetimeText;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(final List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(final Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(final Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(final Clouds clouds) {
        this.clouds = clouds;
    }

    public LastThreeHours getRain() {
        return rain;
    }

    public void setRain(final LastThreeHours rain) {
        this.rain = rain;
    }

    public LastThreeHours getSnow() {
        return snow;
    }

    public void setSnow(final LastThreeHours snow) {
        this.snow = snow;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Consts.EMPTY)
                .omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("datetime", datetime)
                .add("datetimeText", datetimeText)
                .add("weather", weather)
                .add("main", main)
                .add("wind", wind)
                .add("clouds", clouds)
                .add("rain", rain)
                .add("snow", snow)
                .toString();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        @NotNull
        private Integer id;

        @NotEmpty
        private String main;

        @NotEmpty
        private String description;

        @NotEmpty
        private String icon;

        public Integer getId() {
            return id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(final String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(final String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Consts.EMPTY)
                    .omitNullValues()
                    .add("id", id)
                    .add("main", main)
                    .add("description", description)
                    .add("icon", icon)
                    .toString();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @NotNull
        private Double temp;

        @JsonProperty("temp_min")
        private Double tempMin;

        @JsonProperty("temp_max")
        private Double tempMax;

        @NotNull
        private Double humidity;

        @NotNull
        private Double pressure;

        @JsonProperty("sea_level")
        private Double pressureSeaLevel;

        @JsonProperty("grnd_level")
        private Double pressureGrndLevel;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(final Double temp) {
            this.temp = temp;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public void setTempMin(final Double tempMin) {
            this.tempMin = tempMin;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public void setTempMax(final Double tempMax) {
            this.tempMax = tempMax;
        }

        public Double getHumidity() {
            return humidity;
        }

        public void setHumidity(final Double humidity) {
            this.humidity = humidity;
        }

        public Double getPressure() {
            return pressure;
        }

        public void setPressure(final Double pressure) {
            this.pressure = pressure;
        }

        public Double getPressureSeaLevel() {
            return pressureSeaLevel;
        }

        public void setPressureSeaLevel(final Double pressureSeaLevel) {
            this.pressureSeaLevel = pressureSeaLevel;
        }

        public Double getPressureGrndLevel() {
            return pressureGrndLevel;
        }

        public void setPressureGrndLevel(final Double pressureGrndLevel) {
            this.pressureGrndLevel = pressureGrndLevel;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Consts.EMPTY)
                    .omitNullValues()
                    .add("temp", temp)
                    .add("tempMin", tempMin)
                    .add("tempMax", tempMax)
                    .add("humidity", humidity)
                    .add("pressure", pressure)
                    .add("pressureSeaLevel", pressureSeaLevel)
                    .add("pressureGrndLevel", pressureGrndLevel)
                    .toString();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        @NotNull
        private Double speed;

        private Integer deg;

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(final Double speed) {
            this.speed = speed;
        }

        public Integer getDeg() {
            return deg;
        }

        public void setDeg(final Integer deg) {
            this.deg = deg;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Consts.EMPTY)
                    .omitNullValues()
                    .add("speed", speed)
                    .add("deg", deg)
                    .toString();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        @NotNull
        @JsonProperty("all")
        private Double cloudiness;

        public Double getCloudiness() {
            return cloudiness;
        }

        public void setCloudiness(final Double cloudiness) {
            this.cloudiness = cloudiness;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Consts.EMPTY)
                    .omitNullValues()
                    .add("cloudiness", cloudiness)
                    .toString();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LastThreeHours {
        @JsonProperty("3h")
        private Double volume = 0.0;

        public Double getVolume() {
            return volume;
        }

        public void setVolume(final Double volume) {
            this.volume = volume;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Consts.EMPTY)
                    .omitNullValues()
                    .add("volume", volume)
                    .toString();
        }
    }
}
