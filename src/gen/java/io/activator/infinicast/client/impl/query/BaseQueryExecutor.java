package io.activator.infinicast.client.impl.query;
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
    public boolean checkIfHasErrorsAndCallHandlersNew(JObject json, CompleteCallback completeCallback) {
        return ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._connector, json, completeCallback, this._path);
    }
    public void checkIfHasErrorsAndCallHandlersFull(JObject json, CompleteCallback completeCallback) {
        ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersFull(this._connector, json, completeCallback, this._path);
    }
    public static HashMap<String, Integer> getRoleCountDictionary(JObject json) {
        HashMap<String, Integer> roleCount = new HashMap<String, Integer>();
        JArray roleCountArray = json.getJArray("roleCount");
        if ((roleCountArray != null)) {
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
    public void unhandeledError(ErrorInfo errorResults) {
        this._connector.unhandeledErrorInfo(this._path, errorResults);
    }
}
