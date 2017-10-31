package org.panero.common.activemq;

import com.google.common.base.Preconditions;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.util.StringUtils;

public class ActiveMQConnectionFactoryFactory {
    private static final String DEFAULT_NETWORK_BROKER_URL = "tcp://localhost:61616";
    private static final String DEFAULT_EMBEDDED_BROKER_URL = "vm://localhost?broker.persistent=false";

    private final ActiveMQProperties properties;

    private final RedeliveryPolicy redeliveryPolicy;

    public ActiveMQConnectionFactoryFactory(final ActiveMQProperties properties) {
        this(properties, null);
    }

    public ActiveMQConnectionFactoryFactory(final ActiveMQProperties properties, final RedeliveryPolicy redeliveryPolicy) {
        Preconditions.checkArgument(properties != null, "Parameter 'properties' must not be null");
        this.properties = properties;
        this.redeliveryPolicy = redeliveryPolicy;
    }

    public <T extends ActiveMQConnectionFactory> T createConnectionFactory(final Class<T> factoryClass) {
        try {
            final T connectionFactory = doCreateConnectionFactory(factoryClass);
            if (redeliveryPolicy != null) connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
            if (properties.getPrefix() != null) connectionFactory.setClientIDPrefix(properties.getPrefix());
            connectionFactory.setUseCompression(properties.isUseCompression());
            connectionFactory.setUseAsyncSend(properties.isUseAsyncSend());
            ActiveMQPrefetchPolicy prefetchPolicy = connectionFactory.getPrefetchPolicy();
            prefetchPolicy.setQueuePrefetch(properties.getQueueprefetch()); // default 1000
            prefetchPolicy.setQueueBrowserPrefetch(properties.getQueuebrowserprefetch()); // default 500
            prefetchPolicy.setTopicPrefetch(properties.getTopicprefetch()); // default 32766
            prefetchPolicy.setDurableTopicPrefetch(properties.getDurabletopicprefetch()); // default 100
            prefetchPolicy.setOptimizeDurableTopicPrefetch(properties.getOptimizedurabletopicprefetch());
            connectionFactory.setPrefetchPolicy(prefetchPolicy);
            return connectionFactory;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create ActiveMQConnectionFactory", e);
        }
    }

    private <T extends ActiveMQConnectionFactory> T doCreateConnectionFactory(Class<T> factoryClass) throws Exception {
        final String brokerUrl = brokerUrl();
        final String user = properties.getUser();
        final String password = properties.getPassword();
        if (StringUtils.hasLength(user) && StringUtils.hasLength(password)) {
            return factoryClass.getConstructor(String.class, String.class, String.class).newInstance(user, password, brokerUrl);
        }
        return factoryClass.getConstructor(String.class).newInstance(brokerUrl);
    }

    private String brokerUrl() {
        if (StringUtils.hasLength(properties.getBrokerUrl())) return properties.getBrokerUrl();
        if (properties.isInMemory()) return DEFAULT_EMBEDDED_BROKER_URL;
        return DEFAULT_NETWORK_BROKER_URL;
    }
}
