package org.panero.platform.weather;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "panero.integration.weather")
public class WeatherProperties {
    @NotEmpty
    private String apiKey;

    @NotEmpty
    private String units;

    @NotEmpty
    private String lang;

    @NotNull
    private Integer delayCurrent;

    @NotNull
    private Integer delayForecast;

    private List<City> cities;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(final String units) {
        this.units = units;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(final String lang) {
        this.lang = lang;
    }

    public Integer getDelayCurrent() {
        return delayCurrent;
    }

    public void setDelayCurrent(final Integer delayCurrent) {
        this.delayCurrent = delayCurrent;
    }

    public Integer getDelayForecast() {
        return delayForecast;
    }

    public void setDelayForecast(final Integer delayForecast) {
        this.delayForecast = delayForecast;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(final List<City> cities) {
        this.cities = cities;
    }

    public static class City {
        @NotNull
        private Integer id;

        @NotEmpty
        private String tenant;

        public Integer getId() {
            return id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        public String getTenant() {
            return tenant;
        }

        public void setTenant(final String tenant) {
            this.tenant = tenant;
        }
    }
}
