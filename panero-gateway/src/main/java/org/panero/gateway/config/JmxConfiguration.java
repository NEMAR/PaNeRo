package org.panero.gateway.config;

import javax.management.MBeanServer;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.access.MBeanProxyFactoryBean;

@Configuration
@EnableConfigurationProperties(PaneroProperties.class)
public class JmxConfiguration {
    @Bean
    public MBeanProxyFactoryBean brokerProxy(final MBeanServer server, final PaneroProperties properties) throws Exception {
        final String name = "panero-gateway-" + properties.getTenant() + "-" + properties.getClientId();
        final MBeanProxyFactoryBean factoryBean = new MBeanProxyFactoryBean();
        factoryBean.setObjectName("org.apache.activemq:type=Broker,brokerName=" + name);
        factoryBean.setProxyInterface(BrokerViewMBean.class);
        factoryBean.setServer(server);
        return factoryBean;
    }
}
