package io.infinicast.client.api.paths.handler.requests;
import io.infinicast.JObject;
import io.infinicast.*;

public interface IAPResponder {
    void respond(JObject json);
}
