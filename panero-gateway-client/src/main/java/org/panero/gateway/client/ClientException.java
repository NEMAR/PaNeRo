package org.panero.gateway.client;

public class ClientException extends RuntimeException {
    private static final long serialVersionUID = 42L;

    public ClientException() {
    }

    public ClientException(final String message) {
        super(message);
    }

    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClientException(final Throwable cause) {
        super(cause);
    }
}
