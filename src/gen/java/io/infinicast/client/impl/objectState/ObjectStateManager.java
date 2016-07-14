package io.infinicast.client.impl.objectState;

import io.infinicast.client.api.IPath;
import io.infinicast.client.impl.IConnector;

import java.util.HashMap;
public class ObjectStateManager {
    IConnector _connector;
    HashMap<String, IPath> _localObjects = new HashMap<String, IPath>();
    public ObjectStateManager(IConnector apServiceStormConnector) {
        this._connector = apServiceStormConnector;
    }
    public IPath getOrCreateLocalObject(String path) {
        synchronized (this._localObjects) {
            if (!(this._localObjects.containsKey(path))) {
                IPath obj = this._connector.path(path);
                this._localObjects.put(path, obj);
            }
            return this._localObjects.get(path);
        }
    }
}
