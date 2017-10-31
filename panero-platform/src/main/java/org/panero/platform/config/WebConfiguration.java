package org.panero.platform.config;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
public class WebConfiguration {
    @Bean
    public RestTemplate restTemplate(final ProxyProperties properties) {
        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        if (!properties.getType().equals(Proxy.Type.DIRECT)) {
            factory.setProxy(new Proxy(properties.getType(), new InetSocketAddress(properties.getAddress(), properties.getPort())));
        }
        return new RestTemplate(factory);
    }
}
