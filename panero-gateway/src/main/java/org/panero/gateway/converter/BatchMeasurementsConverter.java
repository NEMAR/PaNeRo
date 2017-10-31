package org.panero.gateway.converter;

import org.panero.common.model.BatchMeasurements;
import org.panero.common.model.api.Measurements;
import org.springframework.core.convert.converter.Converter;

public class BatchMeasurementsConverter implements Converter<Measurements, BatchMeasurements> {
    @Override
    public BatchMeasurements convert(final Measurements source) {
        final BatchMeasurements batch = new BatchMeasurements();
        source.getMeasurements().forEach(batch::addMeasurement);
        return batch;
    }
}
