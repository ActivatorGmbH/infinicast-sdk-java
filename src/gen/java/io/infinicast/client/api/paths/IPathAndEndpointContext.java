package io.infinicast.client.api.paths;
import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
import io.infinicast.*;

public interface IPathAndEndpointContext {
    IPath getPath();
    IEndpoint getEndpoint();
    JObject getEndpointData();
}
