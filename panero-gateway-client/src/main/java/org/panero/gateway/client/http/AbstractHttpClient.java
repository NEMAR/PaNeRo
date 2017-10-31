package org.panero.gateway.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.panero.gateway.client.ClientException;
import org.panero.gateway.client.GatewayClient;
import org.panero.gateway.client.http.detail.HttpResponseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractHttpClient implements GatewayClient {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClient.class);

    protected final ObjectMapper objectMapper = new ObjectMapper();

    private final CloseableHttpAsyncClient client;

    public AbstractHttpClient() {
        this(new HttpClientConfiguration());
    }

    public AbstractHttpClient(final HttpClientConfiguration configuration) throws ClientException {
        if (configuration.getConnections() < 1) {
            throw new ClientException("Number of connections for this client must be greater than zero");
        }
        client = HttpAsyncClients.custom()
                .setMaxConnTotal(configuration.getConnections())
                .setMaxConnPerRoute(configuration.getConnections())
                .setSSLContext(configuration.getSslContext())
                .setSSLHostnameVerifier(configuration.getHostnameVerifier())
                .build();
        client.start();
    }

    /**
     * Sends a request in an asynchronous way.
     *
     * @param request The request to be sent
     * @return A future object that can be used to wait for the result
     */
    protected CompletableFuture<Void> sendRequest(final HttpUriRequest request) {
        logger.debug("Sending request: {}", request);
        final CompletableFuture<Void> future = new CompletableFuture<>();
        client.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse response) {
                if (!HttpResponseValidator.getInstance().validate(response)) {
                    logger.debug("Error response has been received: {}", response.getStatusLine());
                    logger.debug("{}", response);
                    failed(new HttpResponseException(response.getStatusLine().getStatusCode(),
                            response.getStatusLine().getReasonPhrase()));
                } else {
                    logger.debug("Request has been successfully sent");
                    future.complete(null);
                }
            }

            @Override
            public void failed(final Exception e) {
                logger.error("Failed to send request: " + e.getMessage(), e);
                future.completeExceptionally(new ClientException("Failed to send request", e));
            }

            @Override
            public void cancelled() {
                future.cancel(true);
            }
        });
        return future;
    }

    protected <T> HttpEntity createHttpEntity(final T payload) throws Exception {
        logger.debug("Creating request body for a payload [{}]", payload.toString());
        return new StringEntity(objectMapper.writeValueAsString(payload), ContentType.APPLICATION_JSON.withCharset("UTF-8"));
    }
}
