package io.infinicast.client.api.paths.handler.objects;

import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IAPathContext;

public interface CreateObjectCallback {
    void accept(ICError icError, JObject data, IAPathContext context);
}
