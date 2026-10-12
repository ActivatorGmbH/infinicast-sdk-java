package io.infinicast.client.api.paths;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
public interface IPathAndEndpointContext {
    IPath getPath();
    IEndpoint getEndpoint();
    JObject getEndpointData();
    /**
     * Core request identifier for an incoming Request callback. Zero for
     * notifications or older context implementations without a request ID.
     */
    default int getRequestId() { return 0; }
}
