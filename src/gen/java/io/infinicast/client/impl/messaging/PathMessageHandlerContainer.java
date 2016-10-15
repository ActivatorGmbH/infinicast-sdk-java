package io.infinicast.client.impl.messaging;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
