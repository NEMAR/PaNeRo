package org.panero.gateway;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@EnableIntegration
@IntegrationComponentScan
@SpringBootApplication
public class Application {
    private static final String PROPERTY_TRUSTSTORE = "javax.net.ssl.trustStore";
    private static final String PROPERTY_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    @Value("${panero.trust-store}")
    private String trustStore;

    @Value("${panero.trust-store-password}")
    private String trustStorePassword;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        System.setProperty(PROPERTY_TRUSTSTORE, trustStore);
        System.setProperty(PROPERTY_TRUSTSTORE_PASSWORD, trustStorePassword);
    }
}
