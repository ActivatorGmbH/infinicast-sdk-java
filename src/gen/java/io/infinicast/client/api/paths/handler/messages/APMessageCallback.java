package io.infinicast.client.api.paths.handler.messages;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface APMessageCallback {
    void accept(JObject json, IPathAndEndpointContext context);
}
