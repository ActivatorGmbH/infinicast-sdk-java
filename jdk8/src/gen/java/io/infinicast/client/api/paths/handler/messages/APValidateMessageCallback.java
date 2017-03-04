package io.infinicast.client.api.paths.handler.messages;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.api.paths.handler.IValidationResponder;
@FunctionalInterface
public interface APValidateMessageCallback {
    void accept(JObject json, IValidationResponder responder, IPathAndEndpointContext context);
}
