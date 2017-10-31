package org.panero.platform.analytics;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.influxdb.dto.QueryResult;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptimizationData {
    @NotEmpty
    List<QueryResult.Series> series;
    @NotBlank
    private String tenant;

    public OptimizationData() {

    }

    public OptimizationData(final String tenant, final List<QueryResult.Series> series) {
        this.tenant = tenant;
        this.series = series;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(final String tenant) {
        this.tenant = tenant;
    }

    public List<QueryResult.Series> getSeries() {
        return series;
    }

    public void setSeries(final List<QueryResult.Series> series) {
        this.series = series;
    }
}
