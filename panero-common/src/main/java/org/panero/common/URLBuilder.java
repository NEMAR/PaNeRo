package org.panero.common;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class URLBuilder {
    private String protocol;
    private String host;
    private Integer port = -1;
    private String path = Consts.EMPTY;
    private Map<String, String> queryParams = new HashMap<>();

    private URLBuilder() {
    }

    public URLBuilder(final URLBuilder builder) {
        this.protocol = builder.protocol;
        this.host = builder.host;
        this.port = builder.port;
        this.path = builder.path;
        this.queryParams = builder.queryParams;
    }

    public static URLBuilder create() {
        return new URLBuilder();
    }

    public URLBuilder http() {
        protocol = "http";
        return this;
    }

    public URLBuilder https() {
        protocol = "https";
        return this;
    }

    public URLBuilder protocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    public URLBuilder host(final String host) {
        this.host = host;
        return this;
    }

    public URLBuilder port(final Integer port) {
        this.port = port;
        return this;
    }

    public URLBuilder path(final String path) {
        this.path = path;
        return this;
    }

    public <T> URLBuilder queryParam(final String name, final T value) {
        queryParams.put(name, String.valueOf(value));
        return this;
    }

    public Actions and() {
        return this.new Actions();
    }

    public class Actions {
        public URL buildIt() {
            Preconditions.checkState(!isNullOrEmpty(protocol), "Parameter 'protocol' must not be empty");
            Preconditions.checkState(!isNullOrEmpty(host), "Parameter 'host' must not be empty");
            try {
                if (!queryParams.isEmpty()) {
                    path = path.concat("?");
                    queryParams.forEach((name, value) -> path = path.concat(name).concat("=").concat(value).concat("&"));
                }
                return new URL(protocol, host, port, path);
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        public URI buildURI() {
            try {
                return buildIt().toURI();
            } catch (URISyntaxException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        public String toString() {
            return buildIt().toString();
        }

        public void consumeIt(Consumer<URL> consumer) {
            consumer.accept(buildIt());
        }
    }
}
