package io.infinicast.client.impl.messaging;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
        this._handlersByType.get(type).addHandler(handler);
    }
    public void callHandlers(ICError error, String type, JObject data, IPathAndEndpointContext context, int requestId) {
        if (this._handlersByType.containsKey(type)) {
            this._handlersByType.get(type).callHandlers(error, data, context, requestId);
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
