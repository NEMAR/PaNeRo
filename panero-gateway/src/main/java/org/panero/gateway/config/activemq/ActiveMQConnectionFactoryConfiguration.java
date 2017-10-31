package org.panero.gateway.config.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.panero.common.activemq.ActiveMQConnectionFactoryFactory;
import org.panero.common.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
class ActiveMQConnectionFactoryConfiguration {
    @Bean
    public CachingConnectionFactory connectionFactory(final ActiveMQProperties properties) {
        final CachingConnectionFactory connectionFactory
                = new CachingConnectionFactory(new ActiveMQConnectionFactoryFactory(properties)
                .createConnectionFactory(ActiveMQConnectionFactory.class));
        connectionFactory.setSessionCacheSize(10);
        return connectionFactory;
    }
}
