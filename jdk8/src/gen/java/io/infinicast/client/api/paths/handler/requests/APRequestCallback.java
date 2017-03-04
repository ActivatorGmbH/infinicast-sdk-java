package io.infinicast.client.api.paths.handler.requests;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface APRequestCallback {
    void accept(JObject json, IAPResponder responder, IPathAndEndpointContext context);
}
