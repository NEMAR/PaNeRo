package org.panero.platform.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.panero.common.BrokerConstants;
import org.panero.common.model.Command;
import org.panero.platform.analytics.DataPreparationTask;
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
public class CommandFlowConfiguration {
    private static Logger logger = LoggerFactory.getLogger(CommandFlowConfiguration.class);

    @Autowired
    private JmsProperties properties;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue commandsQueue;

    @Autowired
    private DataPreparationTask dataPreparationTask;

    @Bean
    public DefaultMessageListenerContainer commandsQueueListener() {
        final DefaultMessageListenerContainer container
                = Jms.container(connectionFactory, commandsQueue).get();
        container.setConcurrentConsumers(properties.getListener().getConcurrency());
        container.setMaxConcurrentConsumers(properties.getListener().getMaxConcurrency());
        container.setErrorHandler(t -> logger.error("Error receiving message: " + t.getMessage(), t.getCause()));
        return container;
    }

    @Bean
    public IntegrationFlow commandsFlow() {
        return IntegrationFlows
                .from(Jms.inboundGateway(commandsQueueListener()))
                .transform(Transformers.fromJson(Command.class))
                .routeToRecipients(r -> r
                        .recipientFlow(s -> s.getHeaders().get(BrokerConstants.COMMAND_HEADER_FIELD, String.class).equals(BrokerConstants.COMMAND_PREPARE_OPTIMIZATION),
                                flow -> flow.handle(dataPreparationTask).transform(Transformers.toJson()))
                )
                .get();
    }
}
