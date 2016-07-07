package io.infinicast.client.api.paths.handler;
import io.infinicast.JObject;
import io.activator.infinicast.*;

@FunctionalInterface
public interface SuccessCallback {
    void accept(JObject data);
}
