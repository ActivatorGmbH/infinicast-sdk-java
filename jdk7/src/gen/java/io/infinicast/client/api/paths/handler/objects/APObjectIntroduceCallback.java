package io.infinicast.client.api.paths.handler.objects;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
public interface APObjectIntroduceCallback {
    void accept(JObject objectData, IPathAndEndpointContext context);
}
