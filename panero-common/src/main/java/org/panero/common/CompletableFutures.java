package org.panero.common;

import java.util.concurrent.CompletableFuture;

public abstract class CompletableFutures {
    public static <T> CompletableFuture<T> completeExceptionally(final Exception e) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }
}
