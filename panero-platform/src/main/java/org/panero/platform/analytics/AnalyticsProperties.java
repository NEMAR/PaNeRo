package org.panero.platform.analytics;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "panero.analytics")
public class AnalyticsProperties {
    @Valid
    private ShortTerm shortTerm;

    public ShortTerm getShortTerm() {
        return shortTerm;
    }

    public void setShortTerm(final ShortTerm shortTerm) {
        this.shortTerm = shortTerm;
    }

    public static class ShortTerm {
        @NotNull
        private Integer interval;

        @Valid
        private List<DataSet> dataSets;

        public Integer getInterval() {
            return interval;
        }

        public void setInterval(final Integer interval) {
            this.interval = interval;
        }

        public List<DataSet> getDataSets() {
            return dataSets;
        }

        public void setDataSets(final List<DataSet> dataSets) {
            this.dataSets = dataSets;
        }
    }

    public static class DataSet {
        @NotBlank
        private String tenant;

        @NotEmpty
        private List<String> queries;

        public String getTenant() {
            return tenant;
        }

        public void setTenant(final String tenant) {
            this.tenant = tenant;
        }

        public List<String> getQueries() {
            return queries;
        }

        public void setQueries(final List<String> queries) {
            this.queries = queries;
        }
    }
}
