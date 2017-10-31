package org.panero.debs.model.converter;

import java.util.List;

import org.panero.common.model.Measurement;
import org.panero.debs.model.Metric;
import org.panero.common.model.api.Measurements;
import org.springframework.core.convert.converter.Converter;

public class MetricToMeasurementsConverter implements Converter<List<? extends Metric>, Measurements> {
    @Override
    public Measurements convert(final List<? extends Metric> source) {
        final Measurements batch = new Measurements();
        source.forEach(m -> convert(m).and().consumeIt(batch::addMeasurement));
        return batch;
    }

    private Measurement.Builder convert(final Metric metric) {
        return Measurement.create(metric.getProperty() == 0 ? "kwh" : "watts")
                .tag("debs", 2014)
                .tag("plug_id", metric.getPlugId())
                .tag("household_id", metric.getHouseholdId())
                .tag("house_id", metric.getHouseId())
                .value(metric.getValue())
                .valueMetadata("property", metric.getProperty());
    }
}
