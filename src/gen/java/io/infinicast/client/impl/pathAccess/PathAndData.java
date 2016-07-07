package io.infinicast.client.impl.pathAccess;
import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
import io.activator.infinicast.*;

public class PathAndData implements IPathAndData {
    JObject _data;
    IPath _path;
    public JObject getData() {
        return this._data;
    }
    public void setData(JObject value) {
        this._data = value;
    }
    public IPath getPath() {
        return this._path;
    }
    public void setPath(IPath value) {
        this._path = value;
    }
}
