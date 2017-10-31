package org.panero.platform.persistence.influxdb;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.panero.common.model.BatchMeasurements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Headers;

@MessageEndpoint
public class InfluxDBPersistence {
    private static Logger logger = LoggerFactory.getLogger(InfluxDBPersistence.class);

    @Autowired
    private InfluxDBTemplate<BatchMeasurements> influxDBTemplate;

    @PostConstruct
    public void init() {
        influxDBTemplate.createDatabase();
    }

    @ServiceActivator
    public void handle(final BatchMeasurements payload, @Headers Map<String, Object> headers) throws Exception {
        logger.info("Handling payload with [{}] measurements for tenant [{}]", payload.getMeasurements().size(), payload.getTenant());
        influxDBTemplate.write(payload);
    }
}
