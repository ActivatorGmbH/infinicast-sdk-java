package io.infinicast.client.impl.contexts;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.IListeningChangedContext;
public class APListeningChangedContext extends APathContext  implements IAPathContext, IListeningChangedContext {
    IEndpoint _endpoint;
    JObject _endpointData;
    public IEndpoint getEndpoint() {
        return this._endpoint;
    }
    public void setEndpoint(IEndpoint value) {
        this._endpoint = value;
    }
    public JObject getEndpointData() {
        return this._endpointData;
    }
    public void setEndpointData(JObject value) {
        this._endpointData = value;
    }
}
