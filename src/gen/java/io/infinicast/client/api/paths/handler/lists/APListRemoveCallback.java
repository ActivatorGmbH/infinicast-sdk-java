package io.infinicast.client.api.paths.handler.lists;
import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.activator.infinicast.*;

@FunctionalInterface
public interface APListRemoveCallback {
    void accept(JObject data, IPathAndEndpointContext context);
}
