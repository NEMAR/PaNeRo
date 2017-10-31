package org.panero.gateway.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.panero.common.BrokerConstants;
import org.panero.common.PersistenceConstants;
import org.panero.common.model.Command;
import org.panero.common.model.api.Measurements;
import org.panero.gateway.converter.BatchMeasurementsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.Transformers;

@Configuration
@EnableConfigurationProperties(PaneroProperties.class)
public class MessageFlowConfiguration {
    private static final String INPUT_MEASUREMENTS = "INPUT_MEASUREMENTS";

    private static final String INPUT_COMMANDS = "INPUT_COMMANDS";
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private Queue plainInputQueue;
    @Autowired
    private Queue commandsQueue;

    @Bean
    public IntegrationFlow defaultFlow(final PaneroProperties properties) {
        return IntegrationFlows
                .from(INPUT_MEASUREMENTS)
                .transform(Transformers.converter(new BatchMeasurementsConverter()))
                .enrichHeaders(h -> h.header(PersistenceConstants.TAG_TENANT, properties.getTenant(), true))
                .enrich(e -> e.property(PersistenceConstants.TAG_TENANT, properties.getTenant()))
                .transform(Transformers.toJson())
                .handle(Jms
                        .outboundAdapter(connectionFactory)
                        .destination(plainInputQueue)
                        .configureJmsTemplate(t -> t
                                .deliveryPersistent(true)
                                .explicitQosEnabled(true)
                                .timeToLive(1000 * 60 * 15)) // 15 minutes
                        .get())
                .get();
    }

    @Bean
    public IntegrationFlow commandsFlow() {
        return IntegrationFlows.from(INPUT_COMMANDS)
                .enrichHeaders(h -> h.headerExpression(BrokerConstants.COMMAND_HEADER_FIELD, "payload.name", true))
                .transform(Transformers.toJson())
                .handle(Jms.outboundGateway(connectionFactory).requestDestination(commandsQueue).get())
                .get();
    }

    @MessagingGateway
    public interface MeasurementsGateway {
        @Gateway(requestChannel = INPUT_MEASUREMENTS)
        void send(final Measurements payload);
    }

    @MessagingGateway
    public interface CommandGateway {
        @Gateway(requestChannel = INPUT_COMMANDS)
        String send(final Command command);
    }
}
