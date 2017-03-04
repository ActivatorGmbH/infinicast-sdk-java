package io.infinicast.client.impl.pathAccess;

import io.infinicast.client.api.IPath;
public interface IPathAndCount {
    int getCount();
    void setCount(int value);
    IPath getPath();
    void setPath(IPath value);
}
