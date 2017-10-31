package org.panero.platform.config;

import java.net.Proxy;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "panero.platform.proxy")
public class ProxyProperties {
    @NotNull
    private Proxy.Type type;

    private String address;

    @Min(1)
    @Max(65535)
    private Integer port;

    public Proxy.Type getType() {
        return type;
    }

    public void setType(final Proxy.Type type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }
}
