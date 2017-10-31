package org.panero.gateway.config;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("panero")
public class PaneroProperties {
    @NotEmpty
    private String tenant;

    @NotEmpty
    private String clientId;

    @Valid
    @NotNull
    private Gateway gateway;

    @Valid
    @NotNull
    private Broker broker;

    @NotEmpty
    private String keyStore;

    @NotEmpty
    private String keyStorePassword;

    @NotEmpty
    private String trustStore;

    @NotEmpty
    private String trustStorePassword;

    public String getTenant() {
        return StringUtils.lowerCase(tenant);
    }

    public void setTenant(final String tenant) {
        this.tenant = tenant;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(final Gateway gateway) {
        this.gateway = gateway;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(final Broker broker) {
        this.broker = broker;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(final String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(final String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(final String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(final String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public static class Gateway {
        @Valid
        @NotNull
        private Protocol http;

        @Valid
        @NotNull
        private Protocol openwire;

        @Valid
        @NotNull
        private Protocol stomp;

        @Valid
        @NotNull
        private Protocol mqtt;

        public Protocol getHttp() {
            return http;
        }

        public void setHttp(final Protocol http) {
            this.http = http;
        }

        public Protocol getOpenwire() {
            return openwire;
        }

        public void setOpenwire(final Protocol openwire) {
            this.openwire = openwire;
        }

        public Protocol getStomp() {
            return stomp;
        }

        public void setStomp(final Protocol stomp) {
            this.stomp = stomp;
        }

        public Protocol getMqtt() {
            return mqtt;
        }

        public void setMqtt(final Protocol mqtt) {
            this.mqtt = mqtt;
        }
    }

    public static class Protocol {
        @NotEmpty
        private String connector;

        @Min(1)
        @Max(65535)
        @NotNull
        private Integer port;

        private String options;

        public String getConnector() {
            return connector;
        }

        public void setConnector(final String connector) {
            this.connector = connector;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(final Integer port) {
            this.port = port;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(final String options) {
            this.options = options;
        }
    }

    public static class Broker {
        @NotEmpty
        private String connector;

        private String options;

        @NotEmpty
        private String hostname;

        @Min(1)
        @Max(65535)
        @NotNull
        private Integer port;

        private String username;

        private String password;

        public String getConnector() {
            return connector;
        }

        public void setConnector(final String connector) {
            this.connector = connector;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(final String options) {
            this.options = options;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(final String hostname) {
            this.hostname = hostname;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(final Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(final String password) {
            this.password = password;
        }
    }
}
