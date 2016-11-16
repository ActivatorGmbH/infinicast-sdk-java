package io.infinicast.client.api.paths.handler;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.ErrorInfo;
@FunctionalInterface
public interface JsonCompletionCallback {
    void accept(ErrorInfo data, JObject json);
}
