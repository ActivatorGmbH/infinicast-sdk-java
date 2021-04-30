package io.infinicast.client.impl.pathAccess;

import io.infinicast.client.api.IPath;
public class PathAndCount implements IPathAndCount {
    int _count;
    IPath _path;
    public int getCount() {
        return this._count;
    }
    public void setCount(int value) {
        this._count = value;
    }
    public IPath getPath() {
        return this._path;
    }
    public void setPath(IPath value) {
        this._path = value;
    }
}
