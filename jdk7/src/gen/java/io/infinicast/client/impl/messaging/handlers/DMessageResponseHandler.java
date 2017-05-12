package io.infinicast.client.impl.messaging.handlers;

import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
public interface DMessageResponseHandler {
    void accept(JObject json, ICError err, IPathAndEndpointContext context);
}
