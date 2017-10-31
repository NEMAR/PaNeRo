package org.panero.platform.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.panero.common.model.BatchMeasurements;
import org.panero.platform.persistence.influxdb.InfluxDBPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
public class PersistenceFlowConfiguration {
    private static Logger logger = LoggerFactory.getLogger(PersistenceFlowConfiguration.class);

    @Autowired
    private JmsProperties properties;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue influxDBPersistenceQueue;

    @Autowired
    private InfluxDBPersistence influxDBPersistence;

    @Bean
    public DefaultMessageListenerContainer influxDBPersistenceQueueListener() {
        final DefaultMessageListenerContainer container
                = Jms.container(connectionFactory, influxDBPersistenceQueue).get();
        container.setConcurrentConsumers(properties.getListener().getConcurrency());
        container.setMaxConcurrentConsumers(properties.getListener().getMaxConcurrency());
        container.setErrorHandler(t -> logger.error("Error receiving message: " + t.getMessage(), t.getCause()));
        return container;
    }

    @Bean
    public IntegrationFlow influxDBPersistenceFlow() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(influxDBPersistenceQueueListener()))
                .transform(message -> {
                    if (message instanceof String) {
                        return ((String) message).replaceAll(",]", "]").replaceAll(",}", "}");
                    }
                    if (message instanceof byte[]) {
                        return new String((byte[])message).replaceAll(",]", "]").replaceAll(",}", "}").getBytes();
                    }
                    return message;
                })
                .transform(Transformers.fromJson(BatchMeasurements.class))
                .handle(influxDBPersistence)
                .get();
    }
}
