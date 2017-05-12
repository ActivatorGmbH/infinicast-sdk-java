package io.infinicast.client.impl.query;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.handler.lists.APListAddCallback;
import io.infinicast.client.api.paths.handler.lists.APListQueryResultCallback;
import io.infinicast.client.api.paths.handler.objects.CreateObjectCallback;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.PathAndEndpointContext;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.impl.messaging.handlers.DMessageResponseHandler;
import io.infinicast.client.impl.pathAccess.IPathAndData;
import io.infinicast.client.impl.pathAccess.PathAndData;
import io.infinicast.client.protocol.Connector2EpsMessageType;

import java.util.ArrayList;
public class ChildQueryExecutor extends BaseQueryExecutor  {
    public ChildQueryExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void getAndListenOnChilden(ICDataQuery query, boolean isRemove, final BiConsumer<JObject, IPathAndEndpointContext> onAdd, final BiConsumer<JObject, IPathAndEndpointContext> onChange, final BiConsumer<JObject, IPathAndEndpointContext> onRemove, boolean isOncePerRole, boolean isSticky) {
        this.getAndListenOnChilden(query, isRemove, onAdd, onChange, onRemove, isOncePerRole, isSticky, (CompleteCallback) null);
    }
    public void findChildren(ICDataQuery query, final APListQueryResultCallback callback) {
        ChildQueryExecutor self = this;
        JObject data = new JObject();
        data.set("query", query.toJson());
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.JsonQuery, super._path, new JObject(data), new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        callback.accept(error, null, 0);
                        ;
                    }
                }
                ))) {
                    ArrayList<IPathAndData> list = new ArrayList<IPathAndData>();
                    int fullCount = json.getInt("fullCount");
                    JArray array = json.getJArray("list");
                    for (JToken ob : array) {
                        PathAndData pathAndData = new PathAndData();
                        pathAndData.setPath(_connector.getObjectStateManager().getOrCreateLocalObject(ob.getString("path")));
                        pathAndData.setData(ob.getJObject("data"));
                        list.add(pathAndData);
                    }
                    callback.accept(null, list, fullCount);
                    ;
                }
                ;
            }
        }
        );
    }
    public void addChild(JObject objectData, String requestedIdentifier, final CreateObjectCallback callback) {
        ChildQueryExecutor self = this;
        JObject data = new JObject();
        data.set("requestedIdentifier", requestedIdentifier);
        data.set("data", objectData);
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.CreateChildRequest, super._path, new JObject(data), new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        callback.accept(error, null, null);
                        ;
                    }
                }
                ))) {
                    callback.accept(null, json.getJObject("data"), getPathAndEndpointContext(context));
                    ;
                }
                ;
            }
        }
        );
    }
    public void findOneOrAddChild(ICDataQuery query, JObject newObjectValue, final QuadConsumer<ICError, JObject, IAPathContext, Boolean> action) {
        ChildQueryExecutor self = this;
        JObject parameters = new JObject();
        parameters.set("data", newObjectValue);
        parameters.set("query", query.toJson());
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetOrCreate, super._path, parameters, new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        action.accept(error, null, null, false);
                        ;
                    }
                }
                ))) {
                    JObject data = json.getJObject("data");
                    action.accept(null, data.getJObject("data"), getPathAndEndpointContext(context), json.getBoolean("newlyCreated"));
                    ;
                }
                ;
            }
        }
        );
    }
    public void modifyAndGetChildrenData(ICDataQuery query, AtomicChange data, final APListQueryResultCallback callback) {
        ChildQueryExecutor self = this;
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        parameters.set("changes", data.toJson());
        if (data.hasNamedQueries()) {
            parameters.set("named", data.getNamedQueryJson());
        }
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.ModifyChildData, super._path, parameters, new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        callback.accept(error, null, 0);
                        ;
                    }
                }
                ))) {
                    ArrayList<IPathAndData> list = new ArrayList<IPathAndData>();
                    int fullCount = json.getInt("fullCount");
                    JArray array = json.getJArray("list");
                    for (JToken ob : array) {
                        PathAndData pathAndData = new PathAndData();
                        pathAndData.setPath(_connector.getObjectStateManager().getOrCreateLocalObject(ob.getString("path")));
                        pathAndData.setData(ob.getJObject("data"));
                        list.add(pathAndData);
                    }
                    callback.accept(null, list, fullCount);
                    ;
                }
                ;
            }
        }
        );
    }
    public void getAndListenOnChilden(ICDataQuery query, boolean isRemove, final BiConsumer<JObject, IPathAndEndpointContext> onAdd, final BiConsumer<JObject, IPathAndEndpointContext> onChange, final BiConsumer<JObject, IPathAndEndpointContext> onRemove, boolean isOncePerRole, boolean isSticky, final CompleteCallback registrationCompleteCallback) {
        ChildQueryExecutor self = this;
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
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetAndListenOnChildren, super._path, parameters, new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        if (registrationCompleteCallback != null) {
                            registrationCompleteCallback.accept(error);
                            ;
                        }
                        ;
                    }
                }
                ))) {
                    JArray array = json.getJArray("list");
                    for (JToken ob : array) {
                        PathAndEndpointContext ctx = new PathAndEndpointContext(_connector.path(ob.getString("path")), context.getEndpoint(), context.getEndpointData());
                        JObject d = ob.getJObject("data");
                        onAdd.accept(d, ctx);
                        ;
                    }
                    if (registrationCompleteCallback != null) {
                        registrationCompleteCallback.accept(null);
                        ;
                    }
                }
                ;
            }
        }
        );
        if (!(isRemove)) {
            super._messageManager.registerHandler(Connector2EpsMessageType.ListAdd, super._path, new DCloudMessageHandler() {
                public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                    if (onAdd != null) {
                        onAdd.accept(json, context);
                        ;
                    }
                    ;
                }
            }
            );
            super._messageManager.registerHandler(Connector2EpsMessageType.ListChange, super._path, new DCloudMessageHandler() {
                public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                    if (onChange != null) {
                        onChange.accept(json, context);
                        ;
                    }
                    ;
                }
            }
            );
            super._messageManager.registerHandler(Connector2EpsMessageType.ListRemove, super._path, new DCloudMessageHandler() {
                public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                    if (onRemove != null) {
                        onRemove.accept(json, context);
                        ;
                    }
                    ;
                }
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
        this.removeChildren(query, (BiConsumer<ICError, Integer>) null);
    }
    public void removeChildren(ICDataQuery query, final BiConsumer<ICError, Integer> completeCallback) {
        ChildQueryExecutor self = this;
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.RemoveChildren, super._path, parameters, new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        if (completeCallback != null) {
                            completeCallback.accept(error, 0);
                            ;
                        }
                        else {
                            _connector.unhandeledErrorInfo(_path, error);
                        }
                        ;
                    }
                }
                ))) {
                    if (completeCallback != null) {
                        completeCallback.accept(null, json.getInt("count"));
                        ;
                    }
                }
                ;
            }
        }
        );
    }
    public void setChildrenData(ICDataQuery query, JObject data) {
        this.setChildrenData(query, data, (BiConsumer<ICError, Integer>) null);
    }
    public void setChildrenData(ICDataQuery query, JObject data, final BiConsumer<ICError, Integer> completeCallback) {
        ChildQueryExecutor self = this;
        JObject parameters = new JObject();
        parameters.set("query", query.toJson());
        parameters.set("data", data);
        ConnectorMessageManager messageManager = super._messageManager;
        messageManager.sendMessageWithResponse(Connector2EpsMessageType.SetChildData, super._path, parameters, new DMessageResponseHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(err, new CompleteCallback() {
                    public void accept(ICError error) {
                        if (completeCallback != null) {
                            completeCallback.accept(error, 0);
                            ;
                        }
                        else {
                            _connector.unhandeledErrorInfo(_path, error);
                        }
                        ;
                    }
                }
                ))) {
                    if (completeCallback != null) {
                        completeCallback.accept(null, json.getInt("fullCount"));
                        ;
                    }
                }
                ;
            }
        }
        );
    }
    void onChildHandler(final APListAddCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback, final Connector2EpsMessageType connector2EpsMessageType) {
        ChildQueryExecutor self = this;
        super._messageManager.addHandler((callback == null), connector2EpsMessageType, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                if (json != null) {
                    Console.WriteLine((((connector2EpsMessageType.toString() + " ") + json.toString()) + " ") + context.getPath().toString());
                }
                else {
                    Console.WriteLine((connector2EpsMessageType.toString() + " null ") + context.getPath().toString());
                }
                callback.accept(json, context);
                ;
            }
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
