package io.infinicast.client.impl.objectState;

import io.infinicast.client.api.IPath;
import io.infinicast.client.impl.IConnector;
public class ObjectStateManager {
    IConnector _connector;
    public ObjectStateManager(IConnector apServiceStormConnector) {
        this._connector = apServiceStormConnector;
    }
    public IPath getOrCreateLocalObject(String path) {
        return this._connector.path(path);
    }
}
