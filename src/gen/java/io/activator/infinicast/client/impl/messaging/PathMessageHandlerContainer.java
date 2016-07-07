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
public class PathMessageHandlerContainer {
    static Logger logger = LoggerFactory.getLogger(PathMessageHandlerContainer.class);
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
    public void callHandlers(JObject data, IPathAndEndpointContext context, int requestId) {
        JObject jsonData = null;
        if ((data != null)) {
            jsonData = new JObject(data);
        }
        synchronized (this._handlers) {
            for (DCloudMessageHandler handler : this._handlers) {
                if ((handler != null)) {
                    handler.accept(jsonData, context, requestId);
                    ;
                }
                else {
                    PathMessageHandlerContainer.logger.error(("null handler for request " + requestId));
                }
            }
        }
    }
    public IPath getPath() {
        return this._path;
    }
}
