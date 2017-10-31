package org.panero.platform.persistence.influxdb.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import org.influxdb.dto.Point;
import org.panero.common.PersistenceConstants;
import org.panero.common.model.BatchMeasurements;
import org.panero.common.model.Measurement;
import org.panero.common.model.NameValuePair;
import org.springframework.context.annotation.Bean;
import org.springframework.data.influxdb.converter.PointCollectionConverter;

public class BatchMeasurementsConverter implements PointCollectionConverter<BatchMeasurements> {
    private final MeasurementConverter delegate = new MeasurementConverter();

    @Bean
    private Set<String> getScalerFloat() {
        return Stream.of("metering_value").collect(Collectors.toSet());
    }

    @Override
    public List<Point> convert(final BatchMeasurements source) {
        if (source == null) return Lists.newArrayList();
        return source.getMeasurements().stream()
                .map((Measurement measurement) -> {
                    measurement.addTag(PersistenceConstants.TAG_TENANT, source.getTenant());
                    source.getTags().forEach(tag -> measurement.addTag(tag.getName(), tag.getValue()));
                    measurement.setValueMetadata(Optional.ofNullable(measurement.getValueMetadata()).orElse(new HashSet<>()).stream()
                            .map(pair -> {
                                // Fix for tenant=saarlouis
                                if ("scaler".equals(pair.getName()) && getScalerFloat().contains(measurement.getName())) {
                                    return new NameValuePair<>(pair.getName(), Float.valueOf(pair.getValue().toString()));
                                }
                                return pair;
                            })
                            .collect(Collectors.toSet()));
                    return delegate.convert(measurement);
                })
                .collect(Collectors.toList());
    }
}
