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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponse {
    @Valid
    private City city = new City();

    @Valid
    @JsonProperty("list")
    private List<WeatherResponse> forecast = Lists.newArrayList();

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    public List<WeatherResponse> getForecast() {
        return forecast;
    }

    public void setForecast(final List<WeatherResponse> forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Consts.EMPTY)
                .omitNullValues()
                .add("city", city)
                .add("forecast", forecast)
                .toString();
    }

    public static class City {
        @NotNull
        private Integer id;

        @NotEmpty
        private String name;

        @NotEmpty
        private String country;

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

        public String getCountry() {
            return country;
        }

        public void setCountry(final String country) {
            this.country = country;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Consts.EMPTY)
                    .omitNullValues()
                    .add("id", id)
                    .add("name", name)
                    .add("country", country)
                    .toString();
        }
    }
}
