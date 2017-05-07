package io.infinicast.client.impl.query;
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
public class BaseQueryExecutor {
    public IPath _path;
    public ConnectorMessageManager _messageManager;
    public IConnector _connector;
    public BaseQueryExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        this._connector = connector;
        this._path = path;
        this._messageManager = messageManager;
    }
    public IAPathContext getPathAndEndpointContext(IPathAndEndpointContext ctx) {
        APathContext context = new APathContext();
        context.setPath(ctx.getPath());
        return context;
    }
    public IAPathContext getPathContext(IPath path) {
        APathContext context = new APathContext();
        context.setPath(path);
        return context;
    }
    public boolean checkIfHasErrorsAndCallHandlersNew(ICError error, CompleteCallback completeCallback) {
        return ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._connector, error, completeCallback, this._path);
    }
    public void checkIfHasErrorsAndCallHandlersFull(ICError error, CompleteCallback completeCallback) {
        ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersFull(this._connector, error, completeCallback, this._path);
    }
    public static HashMap<String, Integer> getRoleCountDictionary(JObject json) {
        HashMap<String, Integer> roleCount = new HashMap<String, Integer>();
        JArray roleCountArray = json.getJArray("roleCount");
        if (roleCountArray != null) {
            for (JToken roleOb : roleCountArray) {
                String role = roleOb.getString("role");
                String handlerType = roleOb.getString("handlerType");
                int count = roleOb.getInt("count");
                if (StringExtensions.areEqual(handlerType, "Message")) {
                    roleCount.put(role, count);
                }
            }
        }
        return roleCount;
    }
    public void unhandeledError(ICError icErrorResults) {
        this._connector.unhandeledErrorInfo(this._path, icErrorResults);
    }
}
