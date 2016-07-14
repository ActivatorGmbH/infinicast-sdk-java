package io.infinicast.client.api.paths.handler.requests;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface APRequestAnswerCallback {
    void accept(ErrorInfo error, JObject json, IPathAndEndpointContext context);
}
