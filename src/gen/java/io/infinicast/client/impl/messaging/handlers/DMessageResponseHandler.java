package io.infinicast.client.impl.messaging.handlers;
import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.*;

@FunctionalInterface
public interface DMessageResponseHandler {
    void accept(JObject json, IPathAndEndpointContext context);
}
