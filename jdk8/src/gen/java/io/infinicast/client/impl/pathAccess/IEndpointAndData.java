package io.infinicast.client.impl.pathAccess;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
public interface IEndpointAndData {
    JObject getData();
    void setData(JObject value);
    IEndpoint getEndpoint();
    void setEndpoint(IEndpoint value);
}
