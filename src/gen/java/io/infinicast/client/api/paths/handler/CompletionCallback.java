package io.infinicast.client.api.paths.handler;

import io.infinicast.client.api.paths.ErrorInfo;
@FunctionalInterface
public interface CompletionCallback {
    void accept(ErrorInfo data);
}
