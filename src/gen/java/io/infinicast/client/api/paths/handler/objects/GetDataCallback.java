package io.infinicast.client.api.paths.handler.objects;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IAPathContext;
@FunctionalInterface
public interface GetDataCallback {
    void accept(ErrorInfo error, JObject json, IAPathContext context);
}
