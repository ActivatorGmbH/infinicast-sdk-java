package io.infinicast.client.impl.contexts;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.*;

public class APathContext implements IAPathContext {
    IPath _path;
    public IPath getPath() {
        return this._path;
    }
    public void setPath(IPath value) {
        this._path = value;
    }
}
