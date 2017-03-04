package io.infinicast.client.api.paths.handler;

import io.infinicast.FunctionalInterface;
import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
@FunctionalInterface
public interface JsonCompletionCallback {
    void accept(ICError data, JObject json);
}
