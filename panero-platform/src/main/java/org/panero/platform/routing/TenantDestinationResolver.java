package org.panero.platform.routing;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
public class TenantDestinationResolver {
    private static final String CHANNEL_PREFIX = "Queue.PANERO.1.";
    private final Map<String, MessageHandler> channelMap = new LinkedHashMap<>();
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * Resolves a channel for a given tenant and a {@link ChannelType}.
     *
     * @param tenant      The tenant identifier
     * @param channelType The type of channel to be resolved
     * @return a JMS-based channel
     */
    public MessageHandler resolve(final String tenant, final ChannelType channelType) {
        String channelName = channelName(tenant, channelType);
        MessageHandler messageHandler = this.channelMap.get(channelName);
        if (messageHandler == null) {
            messageHandler = createMessageHandler(channelName);
        }
        return messageHandler;
    }

    private synchronized MessageHandler createMessageHandler(final String channelName) {
        MessageHandler messageHandler = this.channelMap.get(channelName);
        if (messageHandler == null) {
            messageHandler = Jms
                    .outboundAdapter(connectionFactory)
                    .configureJmsTemplate(t -> t
                            .deliveryPersistent(true)
                            .explicitQosEnabled(true)
                            .timeToLive(1000 * 60 * 60)) // 60 minutes
                    .destination(channelName)
                    .get();
            this.channelMap.put(channelName, messageHandler);
        }
        return messageHandler;
    }

    private String channelName(final String tenant, final ChannelType channelType) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(tenant), "Parameter 'tenant' must not be empty");
        Preconditions.checkNotNull(channelType, "Parameter 'channelType' must not be null");
        return CHANNEL_PREFIX + channelType + "." + tenant;
    }

    public enum ChannelType {
        OPTIMIZATION, CEP;
    }
}
