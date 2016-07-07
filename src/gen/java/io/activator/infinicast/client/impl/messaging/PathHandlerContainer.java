package io.activator.infinicast.client.impl.messaging;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
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
    public void callHandlers(String type, JObject data, IPathAndEndpointContext context, int requestId) {
        if (this._handlersByType.containsKey(type)) {
            this._handlersByType.get(type).callHandlers(data, context, requestId);
        }
    }
    public IPath getPath() {
        return this._path;
    }
    public void removeHandler(String type) {
        this._handlersByType.remove(type);
    }
    public boolean isEmpty() {
        return (this._handlersByType.size() == 0);
    }
}
