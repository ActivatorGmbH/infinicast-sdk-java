package io.infinicast.client.api.paths.handler;

import io.infinicast.FunctionalInterface;
import io.infinicast.JObject;
@FunctionalInterface
public interface SuccessCallback {
    void accept(JObject data);
}
