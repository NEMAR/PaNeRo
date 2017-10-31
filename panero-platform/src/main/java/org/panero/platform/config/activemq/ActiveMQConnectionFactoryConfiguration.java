package org.panero.platform.config.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.panero.common.BrokerConstants;
import org.panero.common.activemq.ActiveMQConnectionFactoryFactory;
import org.panero.common.activemq.ActiveMQProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
class ActiveMQConnectionFactoryConfiguration {
    private static Logger logger = LoggerFactory.getLogger(ActiveMQConnectionFactoryConfiguration.class);

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        return BrokerConstants.DEFAULT_REDELIVERY_POLICY;
    }

    @Bean
    public CachingConnectionFactory connectionFactory(final ActiveMQProperties properties) {
        logger.info("ActiveMQ connection properties: {}", properties);
        final CachingConnectionFactory connectionFactory
                = new CachingConnectionFactory(new ActiveMQConnectionFactoryFactory(properties, redeliveryPolicy())
                .createConnectionFactory(ActiveMQConnectionFactory.class));
        connectionFactory.setSessionCacheSize(10);
        return connectionFactory;
    }
}
