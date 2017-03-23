package io.infinicast.client.api.paths.handler;

import io.infinicast.client.api.errors.ICError;

public interface CompletionCallback {
    void accept(ICError data);
}
