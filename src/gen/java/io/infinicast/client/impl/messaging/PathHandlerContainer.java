package io.infinicast.client.impl.messaging;

import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;

import java.util.HashMap;
public class PathHandlerContainer {
    HashMap<String, PathMessageHandlerContainer> _handlersByType;
    IPath _path;
    public PathHandlerContainer(IPath path) {
        this._path = path;
        this._handlersByType = new HashMap<String, PathMessageHandlerContainer>();
    }
    public void addHandler(String type, DCloudMessageHandler handler) {
        if (!(this._handlersByType.containsKey(type))) {
            this._handlersByType.put(type, new PathMessageHandlerContainer(this._path));
        }
        PathMessageHandlerContainer container = this._handlersByType.get(type);
        container.addHandler(handler);
    }
    public void callHandlers(ICError error, String type, JObject data, IPathAndEndpointContext context, int requestId) {
        if (this._handlersByType.containsKey(type)) {
            PathMessageHandlerContainer container = this._handlersByType.get(type);
            container.callHandlers(error, data, context, requestId);
        }
    }
    public IPath getPath() {
        return this._path;
    }
    public boolean removeHandler(String type) {
        return (null != this._handlersByType.remove(type));
    }
    public boolean isEmpty() {
        return (this._handlersByType.size() == 0);
    }
}
