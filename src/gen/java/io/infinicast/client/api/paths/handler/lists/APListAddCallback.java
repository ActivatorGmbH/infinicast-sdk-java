package io.infinicast.client.api.paths.handler.lists;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface APListAddCallback {
    void accept(JObject data, IPathAndEndpointContext context);
}
