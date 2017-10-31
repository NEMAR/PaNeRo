package org.panero.gateway.client.http.detail;

import com.google.common.collect.Lists;

import java.util.List;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.panero.gateway.client.http.HttpClientConfiguration;

public class ClientConfigurationValidator {
    private static final ClientConfigurationValidator instance = new ClientConfigurationValidator();

    private ClientConfigurationValidator() {
    }

    public static ClientConfigurationValidator getInstance() {
        return instance;
    }

    public List<String> validate(final HttpClientConfiguration configuration) {
        List<String> validation = Lists.newArrayList();

        // Validate hostname
        boolean isHostname = DomainValidator.getInstance(true).isValid(configuration.getHostname());
        boolean isAddress = InetAddressValidator.getInstance().isValid(configuration.getHostname());
        if (!isHostname && !isAddress) {
            validation.add("Hostname must be either a domain name or IP address");
        }

        // Validate port
        if (configuration.getPort() <= 0 || configuration.getPort() > 65535) {
            validation.add("Invalid port number");
        }

        return validation;
    }
}
