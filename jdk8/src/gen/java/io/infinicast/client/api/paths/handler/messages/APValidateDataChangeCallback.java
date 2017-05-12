package io.infinicast.client.api.paths.handler.messages;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.api.paths.handler.IValidationResponder;
@FunctionalInterface
public interface APValidateDataChangeCallback {
    void accept(JObject json, JObject dataBefore, IValidationResponder responder, IPathAndEndpointContext context);
}
