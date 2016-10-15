package io.infinicast.client.impl.messaging.handlers;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface DCloudMessageHandler {
    void accept(JObject json, IPathAndEndpointContext context, int requestId);
}
