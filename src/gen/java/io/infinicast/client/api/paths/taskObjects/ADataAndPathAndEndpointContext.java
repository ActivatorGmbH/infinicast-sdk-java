package io.infinicast.client.api.paths.taskObjects;
import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.*;

public class ADataAndPathAndEndpointContext {
    JObject _data;
    IPathAndEndpointContext _context;
    public JObject getData() {
        return this._data;
    }
    public void setData(JObject value) {
        this._data = value;
    }
    public IPathAndEndpointContext getContext() {
        return this._context;
    }
    public void setContext(IPathAndEndpointContext value) {
        this._context = value;
    }
}
