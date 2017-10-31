package org.panero.gateway.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.panero.common.model.Measurement;
import org.panero.common.model.api.Measurements;

/**
 * Gateway client interface
 *
 * @author Michael Wurster
 */
public interface GatewayClient {
    /**
     * Sends a {@link Measurement} payload to the Gateway API.  The function call will block
     * until the event has been sent, or an error occurs.
     *
     * @param payload The payload to be sent
     * @throws ClientException If an error occurs during sending the paylaod
     */
    void send(final Measurements payload) throws ClientException;

    /**
     * Sends a {@link Measurement} payload to the Gateway API.  The function call will not
     * block.  Instead, the resulting future object gets resolved on success. On error, a
     * {@link ClientException}, wrapped inside a {@link ExecutionException} will be thrown at
     * the place where the <pre>get</pre> operation of the future object is called.
     *
     * @param payload The payload to be sent
     * @return Returns a {@link CompletableFuture}
     * @throws ClientException If an error occurs during sending the paylaod
     */
    CompletableFuture<Void> sendAsync(final Measurements payload);

    /**
     * Sends a bulk of {@link Measurement} to the Gateway API.  The function call will
     * block until all events have been sent, or an error occurs.
     *
     * @param payload The bulk to be sent
     * @throws ClientException If an error occurs during sending the bulk
     */
    void send(final List<Measurements> payload) throws ClientException;

    /**
     * Sends a bulk of {@link Measurement} to the Gateway API.  The function call will not
     * block.  Instead, the resulting future object gets resolved on success. On error, a
     * {@link ClientException}, wrapped inside a {@link ExecutionException} will be thrown at
     * the place where the <pre>get</pre> operation of the future object is called.
     *
     * @param payload The bulk to be sent
     * @return Returns a {@link CompletableFuture}
     * @throws ClientException If an error occurs during sending the bulk
     */
    CompletableFuture<Void> sendAsync(final List<Measurements> payload);
}
