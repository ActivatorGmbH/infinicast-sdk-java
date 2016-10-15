package io.infinicast.client.api.paths.handler;

import io.infinicast.JObject;
@FunctionalInterface
public interface SuccessCallback {
    void accept(JObject data);
}
