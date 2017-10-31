package org.panero.platform.weather;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.panero.common.model.BatchMeasurements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.Transformers;

@Configuration
public class WeatherFlowConfiguration {
    private static final String INPUT_WEATHER = "INPUT_WEATHER";
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private Queue plainInputQueue;

    @Bean
    public IntegrationFlow weatherFlow() {
        return IntegrationFlows
                .from(INPUT_WEATHER)
                .transform(Transformers.toJson())
                .handle(Jms.outboundAdapter(connectionFactory).destination(plainInputQueue).get())
                .get();
    }

    @MessagingGateway
    public interface WeatherGateway {
        @Gateway(requestChannel = INPUT_WEATHER)
        void send(final BatchMeasurements payload);
    }
}
