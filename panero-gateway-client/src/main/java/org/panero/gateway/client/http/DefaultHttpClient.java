package org.panero.gateway.client.http;

import com.google.common.collect.Lists;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.panero.common.CompletableFutures;
import org.panero.common.URLBuilder;
import org.panero.gateway.client.ClientException;
import org.panero.gateway.client.http.detail.ClientConfigurationValidator;
import org.panero.common.model.api.Measurements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpClient extends AbstractHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpClient.class);

    private final URI baseURI;

    public DefaultHttpClient(final HttpClientConfiguration configuration) throws ClientException {
        super(configuration);

        // Validate configuration
        logger.debug("Validating configuration object: {}", configuration);
        final List<String> validationResult = ClientConfigurationValidator.getInstance().validate(configuration);
        if (validationResult.size() > 0) {
            logger.error("Invalid configuration: {}", StringUtils.join(validationResult, " "));
            throw new ClientException("Invalid configuration: " + StringUtils.join(validationResult, " "));
        }

        // Init
        this.baseURI = URLBuilder.create()
                .protocol(configuration.isSslEnabled() ? "https" : "http")
                .host(configuration.getHostname())
                .port(configuration.getPort())
                .path("/write")
                .and()
                .buildURI();

        logger.debug("Created HTTP client, using URI: {}", baseURI);
    }

    @Override
    public void send(final Measurements payload) throws ClientException {
        try {
            sendAsync(payload).get();
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof ClientException) throw (ClientException) e.getCause();
            throw new ClientException("Failed to send request", e);
        }
    }

    @Override
    public CompletableFuture<Void> sendAsync(final Measurements payload) {
        logger.debug("Sending single payload [{}]", payload);
        HttpPost request = new HttpPost(baseURI);
        try {
            request.setEntity(createHttpEntity(payload));
        } catch (Exception e) {
            logger.error("Failed to prepare request", e);
            return CompletableFutures.completeExceptionally(e);
        }
        return sendRequest(request);
    }

    @Override
    public void send(final List<Measurements> payload) throws ClientException {
        try {
            sendAsync(payload).get();
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof ClientException) throw (ClientException) e.getCause();
            throw new ClientException("Failed to send bulk of measurements", e);
        }
    }

    @Override
    public CompletableFuture<Void> sendAsync(final List<Measurements> payload) {
        final List<CompletableFuture<Void>> futures = Lists.newArrayList();
        payload.forEach(measurement -> futures.add(sendAsync(measurement)));
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }
}
