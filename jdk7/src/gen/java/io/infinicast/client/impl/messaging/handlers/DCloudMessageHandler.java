package io.infinicast.client.impl.messaging.handlers;

import io.infinicast.FunctionalInterface;
import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface DCloudMessageHandler {
    void accept(JObject json, ICError error, IPathAndEndpointContext context, int requestedId);
}
