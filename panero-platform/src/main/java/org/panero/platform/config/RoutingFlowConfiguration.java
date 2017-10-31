package org.panero.platform.config;

import java.util.concurrent.Executors;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.panero.common.PersistenceConstants;
import org.panero.common.model.BatchMeasurements;
import org.panero.platform.routing.TenantDestinationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.StringUtils;

@Configuration
public class RoutingFlowConfiguration {
    private static Logger logger = LoggerFactory.getLogger(RoutingFlowConfiguration.class);

    @Autowired
    private JmsProperties properties;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue plainInputQueue;

    @Autowired
    private Queue normalizedInputQueue;

    @Autowired
    private Queue influxDBPersistenceQueue;

    @Autowired
    private Queue hanaDBPersistenceQueue;

    @Autowired
    private Queue mesapPersistenceQueue;

    @Autowired
    private Queue csvPersistenceQueue;

    @Autowired
    private TenantDestinationResolver destinationResolver;


    // Message Listener *********************************************************

    @Bean
    public DefaultMessageListenerContainer plainInputQueueListener() {
        final DefaultMessageListenerContainer container
                = Jms.container(connectionFactory, plainInputQueue).get();
        container.setConcurrentConsumers(properties.getListener().getConcurrency());
        container.setMaxConcurrentConsumers(properties.getListener().getMaxConcurrency());
        container.setErrorHandler(t -> logger.error("Error receiving message: " + t.getMessage(), t.getCause()));
        return container;
    }

    @Bean
    public DefaultMessageListenerContainer normalizedInputQueueListener() {
        final DefaultMessageListenerContainer container
                = Jms.container(connectionFactory, normalizedInputQueue).get();
        container.setConcurrentConsumers(properties.getListener().getConcurrency());
        container.setMaxConcurrentConsumers(properties.getListener().getMaxConcurrency());
        container.setErrorHandler(t -> logger.error("Error receiving message: " + t.getMessage(), t.getCause()));
        return container;
    }


    // Message Handler **********************************************************

    @Bean
    public MessageHandler normalizedMessageHandler() {
        return Jms.outboundAdapter(connectionFactory).destination(normalizedInputQueue).get();
    }

    @Bean
    public MessageHandler influxDBPersistenceMessageHandler() {
        return Jms.outboundAdapter(connectionFactory).destination(influxDBPersistenceQueue).get();
    }

    @Bean
    public MessageHandler hanaDBPersistenceMessageHandler() {
        return Jms.outboundAdapter(connectionFactory).destination(hanaDBPersistenceQueue).get();
    }

    @Bean
    public MessageHandler mesapPersistenceMessageHandler() {
        return Jms.outboundAdapter(connectionFactory)
                .destination(mesapPersistenceQueue)
                .configureJmsTemplate(t -> t
                        .deliveryPersistent(true)
                        .explicitQosEnabled(true)
                        .timeToLive(1000 * 60 * 5)) // 5 minutes
                .get();
    }

    @Bean
    public MessageHandler csvPersistenceMessageHandler() {
        return Jms.outboundAdapter(connectionFactory)
                .destination(csvPersistenceQueue)
                .configureJmsTemplate(t -> t
                        .deliveryPersistent(true)
                        .explicitQosEnabled(true)
                        .timeToLive(1000 * 60 * 5)) // 5 minutes
                .get();
    }


    // Message Flow *************************************************************

    @Bean
    public IntegrationFlow plainInputFlow() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(plainInputQueueListener()))
                .transform(Transformers.fromJson(BatchMeasurements.class))
                .<BatchMeasurements>filter(p -> StringUtils.hasLength(p.getTenant()))

      /*
       * Add "normalizer" component here ...
       */

                .enrichHeaders(h -> h.headerExpression(PersistenceConstants.TAG_TENANT, "payload.tenant", false))
                .filter("headers['tenant'] != null")
                .transform(Transformers.toJson())
                .handle(normalizedMessageHandler())
                .get();
    }

    @Bean
    public IntegrationFlow normalizedInputFlow() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(normalizedInputQueueListener()))
                .routeToRecipients(r -> r

                        // Forward messages to persistence layer
                        .recipientFlow(s -> true, flow -> flow.handle(influxDBPersistenceMessageHandler()))
                        .recipientFlow(s -> true, flow -> flow.handle(mesapPersistenceMessageHandler()))
                        .recipientFlow(s -> true, flow -> flow.handle(csvPersistenceMessageHandler()))
                        .recipientFlow(s -> true, flow -> flow.handle(hanaDBPersistenceMessageHandler()))

                        // Forward messages to tenant-based routing component
                        .recipient(tenantRouterChannel()))

                .get();
    }

    @Bean
    public IntegrationFlow tenantRoutingFlow() {
        return IntegrationFlows
                .from(tenantRouterChannel())
                .handle(message -> {
                    final String tenant = String.valueOf(message.getHeaders().get(PersistenceConstants.TAG_TENANT));
                    final MessageHandler messageHandler = destinationResolver.resolve(tenant, TenantDestinationResolver.ChannelType.CEP);
                    messageHandler.handleMessage(message);
                })
                .get();
    }


    // Internals ****************************************************************

    @Bean
    public MessageChannel tenantRouterChannel() {
        return MessageChannels.executor(Executors.newCachedThreadPool()).get();
    }
}
