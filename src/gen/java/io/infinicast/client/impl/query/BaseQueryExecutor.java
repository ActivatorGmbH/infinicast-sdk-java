package io.infinicast.client.impl.query;

import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.JToken;
import io.infinicast.StringExtensions;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.APathContext;
import io.infinicast.client.impl.helper.ErrorHandlingHelper;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;

import java.util.HashMap;
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
    public void unhandeledError(ErrorInfo errorResults) {
        this._connector.unhandeledErrorInfo(this._path, errorResults);
    }
}
