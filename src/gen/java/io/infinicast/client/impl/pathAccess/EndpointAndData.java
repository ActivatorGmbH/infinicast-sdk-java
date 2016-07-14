package io.infinicast.client.impl.pathAccess;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
public class EndpointAndData implements IEndpointAndData {
    JObject _data;
    IEndpoint _endpoint;
    public JObject getData() {
        return this._data;
    }
    public void setData(JObject value) {
        this._data = value;
    }
    public IEndpoint getEndpoint() {
        return this._endpoint;
    }
    public void setEndpoint(IEndpoint value) {
        this._endpoint = value;
    }
}
