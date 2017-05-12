package io.infinicast.client.impl.contexts;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.IListeningStartedContext;

import java.util.HashMap;
public class APListeningStartedContext extends APathContext  implements IAPathContext, IListeningStartedContext {
    public HashMap<String, Integer> listenerCount = null;
    IEndpoint _endpoint;
    JObject _endpointData;
    public int getListenerCount(String role) {
        if ((this.listenerCount == null) || !(this.listenerCount.containsKey(role))) {
            return 0;
        }
        return this.listenerCount.get(role);
    }
    public boolean isFirstListenerOfRole(String role) {
        return (this.getListenerCount(role) == 1);
    }
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
