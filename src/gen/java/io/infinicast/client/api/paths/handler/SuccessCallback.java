package io.infinicast.client.api.paths.handler;
import io.infinicast.JObject;
import io.infinicast.*;

@FunctionalInterface
public interface SuccessCallback {
    void accept(JObject data);
}
