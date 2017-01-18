package io.infinicast.client.api.paths.handler.requests;

import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
public interface IAPResponder {
    void respond(JObject json);
    void respondWithError(ICError error);
}
