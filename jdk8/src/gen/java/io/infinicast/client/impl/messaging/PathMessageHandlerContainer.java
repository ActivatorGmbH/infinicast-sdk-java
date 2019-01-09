package io.infinicast.client.impl.messaging;

import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
public class PathMessageHandlerContainer {
    private static Logger _logger = LoggerFactory.getLogger(PathMessageHandlerContainer.class);
    ArrayList<DCloudMessageHandler> _handlers;
    IPath _path;
    public PathMessageHandlerContainer(IPath path) {
        this._path = path;
        this._handlers = new ArrayList<DCloudMessageHandler>();
    }
    public void addHandler(DCloudMessageHandler handler) {
        synchronized (this._handlers) {
            this._handlers.add(handler);
        }
    }
    public void callHandlers(ICError error, JObject data, IPathAndEndpointContext context, int requestId) {
        JObject jsonData = null;
        if (data != null) {
            jsonData = new JObject(data);
        }
        synchronized (this._handlers) {
            for (DCloudMessageHandler handler : this._handlers) {
                if (handler != null) {
                    handler.accept(jsonData, error, context, requestId);
                    ;
                }
                else {
                    PathMessageHandlerContainer._logger.error("null handler for request " + requestId);
                }
            }
        }
    }
    public IPath getPath() {
        return this._path;
    }
}
