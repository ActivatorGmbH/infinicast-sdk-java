package io.infinicast.client.api.paths.handler.requests;

import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface APRequestAnswerCallback {
    void accept(ICError icError, JObject json, IPathAndEndpointContext context);
}
