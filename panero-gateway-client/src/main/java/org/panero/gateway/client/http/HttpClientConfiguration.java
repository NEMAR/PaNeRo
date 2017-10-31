package org.panero.gateway.client.http;

import com.google.common.base.MoreObjects;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.panero.gateway.client.GatewayConfiguration;
import org.panero.gateway.client.http.detail.LaxHostnameVerifier;
import org.panero.gateway.client.http.detail.LaxTrustManager;

public class HttpClientConfiguration implements GatewayConfiguration {
    public static final String DEFAULT_HOSTNAME = "localhost";
    public static final int DEFAULT_PORT = 8080;
    public static final boolean DEFAULT_SSL_ENABLED = false;
    public static final int DEFAULT_CONNECTIONS = 30;

    private String hostname = DEFAULT_HOSTNAME;
    private int port = DEFAULT_PORT;
    private boolean sslEnabled = DEFAULT_SSL_ENABLED;
    private int connections = DEFAULT_CONNECTIONS;

    private SSLContext sslContext = null;
    private HostnameVerifier hostnameVerifier = null;

    public HttpClientConfiguration() {
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new LaxTrustManager()}, null);
            hostnameVerifier = new LaxHostnameVerifier();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException("Unable to create SSL context!", e);
        }
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(final boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(final int connections) {
        this.connections = connections;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setSslContext(final SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(final HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("hostname", hostname)
                .add("port", port)
                .add("ssl", sslEnabled)
                .add("connections", connections)
                .toString();
    }
}
