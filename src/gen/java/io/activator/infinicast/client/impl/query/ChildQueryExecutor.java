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
public class ChildQueryExecutor extends BaseQueryExecutor  {
    public ChildQueryExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void getAndListenOnChilden(ICDataQuery query, boolean isRemove, BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onChange, BiConsumer<JObject, IPathAndEndpointContext> onRemove, boolean isOncePerRole, boolean isSticky) {
        this.getAndListenOnChilden(query, isRemove, onAdd, onChange, onRemove, isOncePerRole, isSticky, (CompleteCallback) null);
    }
    public void findChildren(ICDataQuery query, APListQueryResultCallback callback) {
        JObject data = new JObject();
        data.set("query", query.toJson());
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.JsonQuery, super._path, new JObject(data), (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                callback.accept(error, null, 0);
                ;
            }
            ))) {
                ArrayList<IPathAndData> list = new ArrayList<IPathAndData>();
                int fullCount = json.getInt("fullCount");
                JArray array = json.getJArray("list");
                for (JToken ob : array) {
                    PathAndData pathAndData = new PathAndData();
                    pathAndData.setPath(super._connector.getObjectStateManager().getOrCreateLocalObject(ob.getString("path")));
                    pathAndData.setData(ob.getJObject("data"));
                    list.add(pathAndData);
                }
                callback.accept(null, list, fullCount);
                ;
            }
            ;
        }
        );
    }
    public void addChild(JObject objectData, String requestedIdentifier, CreateObjectCallback callback) {
        JObject data = new JObject();
        data.set("requestedIdentifier", requestedIdentifier);
        data.set("data", objectData);
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.CreateChildRequest, super._path, new JObject(data), (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                callback.accept(error, null, null);
                ;
            }
            ))) {
                callback.accept(null, json.getJObject("data"), super.getPathAndEndpointContext(context));
                ;
            }
            ;
        }
        );
    }
    public void findOneOrAddChild(ICDataQuery query, JObject newObjectValue, QuadConsumer<ErrorInfo, JObject, IAPathContext, Boolean> action) {
        JObject parameters = new JObject();
        parameters.set("data", newObjectValue);
        parameters.set("query", query.toJson());
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetOrCreate, super._path, parameters, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                action.accept(error, null, null, false);
                ;
            }
            ))) {
                JObject data = json.getJObject("data");
                action.accept(null, data.getJObject("data"), super.getPathAndEndpointContext(context), json.getBoolean("newlyCreated"));
                ;
            }
            ;
        }
        );
    }
    public void modifyAndGetChildrenData(ICDataQuery query, AtomicChange data, APListQueryResultCallback callback) {
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        parameters.set("changes", data.toJson());
        if (data.hasNamedQueries()) {
            parameters.set("named", data.getNamedQueryJson());
        }
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.ModifyChildData, super._path, parameters, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                callback.accept(error, null, 0);
                ;
            }
            ))) {
                ArrayList<IPathAndData> list = new ArrayList<IPathAndData>();
                int fullCount = json.getInt("fullCount");
                JArray array = json.getJArray("list");
                for (JToken ob : array) {
                    PathAndData pathAndData = new PathAndData();
                    pathAndData.setPath(super._connector.getObjectStateManager().getOrCreateLocalObject(ob.getString("path")));
                    pathAndData.setData(ob.getJObject("data"));
                    list.add(pathAndData);
                }
                callback.accept(null, list, fullCount);
                ;
            }
            ;
        }
        );
    }
    public void getAndListenOnChilden(ICDataQuery query, boolean isRemove, BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onChange, BiConsumer<JObject, IPathAndEndpointContext> onRemove, boolean isOncePerRole, boolean isSticky, CompleteCallback registrationCompleteCallback) {
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        if (isRemove) {
            parameters.set("remove", true);
        }
        if (isOncePerRole) {
            parameters.set("once", true);
        }
        if (isSticky) {
            parameters.set("sticky", true);
        }
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetAndListenOnChildren, super._path, parameters, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                if ((registrationCompleteCallback != null)) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                ;
            }
            ))) {
                JArray array = json.getJArray("list");
                for (JToken ob : array) {
                    PathAndEndpointContext ctx = new PathAndEndpointContext(super._connector.path(ob.getString("path")), context.getEndpoint(), context.getEndpointData());
                    JObject d = ob.getJObject("data");
                    onAdd.accept(d, ctx);
                    ;
                }
                if ((registrationCompleteCallback != null)) {
                    registrationCompleteCallback.accept(null);
                    ;
                }
            }
            ;
        }
        );
        if (!(isRemove)) {
            super._messageManager.registerHandler(Connector2EpsMessageType.ListAdd, super._path, (json, context, id) -> {
                if ((onAdd != null)) {
                    onAdd.accept(json, context);
                    ;
                }
                ;
            }
            );
            super._messageManager.registerHandler(Connector2EpsMessageType.ListChange, super._path, (json, context, id) -> {
                if ((onChange != null)) {
                    onChange.accept(json, context);
                    ;
                }
                ;
            }
            );
            super._messageManager.registerHandler(Connector2EpsMessageType.ListRemove, super._path, (json, context, id) -> {
                if ((onRemove != null)) {
                    onRemove.accept(json, context);
                    ;
                }
                ;
            }
            );
        }
        else {
            super._messageManager.registerHandler(Connector2EpsMessageType.ListAdd, super._path, null);
            super._messageManager.registerHandler(Connector2EpsMessageType.ListRemove, super._path, null);
            super._messageManager.registerHandler(Connector2EpsMessageType.ListChange, super._path, null);
        }
    }
    public void removeChildren(ICDataQuery query) {
        this.removeChildren(query, (BiConsumer<ErrorInfo, Integer>) null);
    }
    public void removeChildren(ICDataQuery query, BiConsumer<ErrorInfo, Integer> completeCallback) {
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.RemoveChildren, super._path, parameters, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                if ((completeCallback != null)) {
                    completeCallback.accept(error, 0);
                    ;
                }
                else {
                    super._connector.unhandeledErrorInfo(super._path, error);
                }
                ;
            }
            ))) {
                if ((completeCallback != null)) {
                    completeCallback.accept(null, json.getInt("count"));
                    ;
                }
            }
            ;
        }
        );
    }
    public void setChildrenData(ICDataQuery query, JObject data) {
        this.setChildrenData(query, data, (BiConsumer<ErrorInfo, Integer>) null);
    }
    public void setChildrenData(ICDataQuery query, JObject data, BiConsumer<ErrorInfo, Integer> completeCallback) {
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        parameters.set("data", data);
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.SetChildData, super._path, parameters, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                if ((completeCallback != null)) {
                    completeCallback.accept(error, 0);
                    ;
                }
                else {
                    super._connector.unhandeledErrorInfo(super._path, error);
                }
                ;
            }
            ))) {
                if ((completeCallback != null)) {
                    completeCallback.accept(null, json.getInt("fullCount"));
                    ;
                }
            }
            ;
        }
        );
    }
    void onChildHandler(APListAddCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback, Connector2EpsMessageType connector2EpsMessageType) {
        super._messageManager.addHandler((callback == null), connector2EpsMessageType, super._path, (json, context, id) -> {
            if ((json != null)) {
                Console.WriteLine(((((connector2EpsMessageType.toString() + " ") + json.toString()) + " ") + context.getPath().toString()));
            }
            else {
                Console.WriteLine(((connector2EpsMessageType.toString() + " null ") + context.getPath().toString()));
            }
            callback.accept(json, context);
            ;
        }
        , completeCallback, options);
    }
    public void onChildAdd(APListAddCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        this.onChildHandler(callback, options, completeCallback, Connector2EpsMessageType.ListAdd);
    }
    public void onChildAdd(APListAddCallback callback, HandlerRegistrationOptions options) {
        this.onChildAdd(callback, options, (CompleteCallback) null);
    }
    public void onChildAdd(APListAddCallback callback) {
        this.onChildAdd(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onChildChange(APListAddCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        this.onChildHandler(callback, options, completeCallback, Connector2EpsMessageType.ListChange);
    }
    public void onChildChange(APListAddCallback callback, HandlerRegistrationOptions options) {
        this.onChildChange(callback, options, (CompleteCallback) null);
    }
    public void onChildChange(APListAddCallback callback) {
        this.onChildChange(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onChildDelete(APListAddCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        this.onChildHandler(callback, options, completeCallback, Connector2EpsMessageType.ListRemove);
    }
    public void onChildDelete(APListAddCallback callback, HandlerRegistrationOptions options) {
        this.onChildDelete(callback, options, (CompleteCallback) null);
    }
    public void onChildDelete(APListAddCallback callback) {
        this.onChildDelete(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
}
