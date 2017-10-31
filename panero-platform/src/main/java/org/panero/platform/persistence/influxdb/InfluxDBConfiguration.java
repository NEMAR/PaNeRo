package org.panero.platform.persistence.influxdb;

import org.panero.common.model.BatchMeasurements;
import org.panero.platform.persistence.influxdb.converter.BatchMeasurementsConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.influxdb.InfluxDBConnectionFactory;
import org.springframework.data.influxdb.InfluxDBProperties;
import org.springframework.data.influxdb.InfluxDBTemplate;

@Configuration
@EnableConfigurationProperties(InfluxDBProperties.class)
@ConditionalOnProperty(prefix = "panero", name = "database", havingValue = "influxdb", matchIfMissing = true)
public class InfluxDBConfiguration {
    private static Logger logger = LoggerFactory.getLogger(InfluxDBConfiguration.class);

    @Bean
    public InfluxDBConnectionFactory influxDBConnectionFactory(final InfluxDBProperties properties) {
        logger.info("InfluxDB connection properties: {}", properties);
        return new InfluxDBConnectionFactory(properties);
    }

    @Bean
    public InfluxDBTemplate<BatchMeasurements> influxDBTemplate(final InfluxDBConnectionFactory connectionFactory) {
        return new InfluxDBTemplate<>(connectionFactory, new BatchMeasurementsConverter());
    }
}
