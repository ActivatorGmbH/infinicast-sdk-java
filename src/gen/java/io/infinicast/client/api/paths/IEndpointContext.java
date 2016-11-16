package io.infinicast.client.api.paths;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
public interface IEndpointContext {
    IEndpoint getEndpoint();
    JObject getEndpointData();
}
