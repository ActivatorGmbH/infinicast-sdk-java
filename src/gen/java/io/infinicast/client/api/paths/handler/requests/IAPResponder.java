package io.infinicast.client.api.paths.handler.requests;
import io.infinicast.JObject;
import io.activator.infinicast.*;

public interface IAPResponder {
    void respond(JObject json);
}
