package io.infinicast.client.impl.pathAccess;

import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
public interface IPathAndData {
    JObject getData();
    void setData(JObject value);
    IPath getPath();
    void setPath(IPath value);
}
