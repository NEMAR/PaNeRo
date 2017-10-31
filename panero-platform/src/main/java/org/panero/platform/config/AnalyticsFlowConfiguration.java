package org.panero.platform.config;

import org.panero.common.PersistenceConstants;
import org.panero.platform.analytics.OptimizationData;
import org.panero.platform.routing.TenantDestinationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.messaging.MessageHandler;

@Configuration
public class AnalyticsFlowConfiguration {
    private static final String INPUT_OPTIMIZATION_DATA = "INPUT_OPTIMIZATION_DATA";
    @Autowired
    private TenantDestinationResolver destinationResolver;

    @Bean
    public IntegrationFlow dataPreparationFlow() {
        return IntegrationFlows
                .from(INPUT_OPTIMIZATION_DATA)
                .enrichHeaders(h -> h.headerExpression(PersistenceConstants.TAG_TENANT, "payload.tenant", false))
                .transform(Transformers.toJson())
                .handle(message -> {
                    final String tenant = String.valueOf(message.getHeaders().get(PersistenceConstants.TAG_TENANT));
                    final MessageHandler messageHandler = destinationResolver.resolve(tenant, TenantDestinationResolver.ChannelType.OPTIMIZATION);
                    messageHandler.handleMessage(message);
                })
                .get();
    }

    @MessagingGateway
    public interface OptimizationDataGateway {
        @Gateway(requestChannel = INPUT_OPTIMIZATION_DATA)
        void send(final OptimizationData payload);
    }
}
