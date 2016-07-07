package io.infinicast.client.impl.contexts;
import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.activator.infinicast.*;

public class PathAndEndpointContext implements IPathAndEndpointContext {
    IPath _path;
    IEndpoint _endpoint;
    JObject _endpointData;
    public PathAndEndpointContext(IPath path_, IEndpoint endpointOb_, JObject endpointData_) {
        this.setPath(path_);
        this.setEndpoint(endpointOb_);
        this.setEndpointData(endpointData_);
    }
    public IPath getPath() {
        return this._path;
    }
    public void setPath(IPath value) {
        this._path = value;
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
