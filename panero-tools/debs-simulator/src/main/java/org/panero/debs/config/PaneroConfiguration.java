package org.panero.debs.config;

import org.panero.gateway.client.GatewayClient;
import org.panero.gateway.client.GatewayConfiguration;
import org.panero.gateway.client.http.DefaultHttpClient;
import org.panero.gateway.client.http.HttpClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PaneroProperties.class)
public class PaneroConfiguration {
    @Autowired
    private PaneroProperties properties;

    @Bean
    public GatewayConfiguration gatewayConfiguration() {
        final HttpClientConfiguration clientConfiguration = new HttpClientConfiguration();
        clientConfiguration.setHostname(properties.getGateway().getHostname());
        clientConfiguration.setPort(properties.getGateway().getPort());
        return clientConfiguration;
    }

    @Bean
    public GatewayClient gatewayClient(final GatewayConfiguration configuration) {
        return new DefaultHttpClient((HttpClientConfiguration) configuration);
    }
}
