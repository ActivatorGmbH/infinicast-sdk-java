package io.infinicast.client.impl.query;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.query.ListeningType;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.APListeningChangedContext;
import io.infinicast.client.impl.contexts.APListeningEndedContext;
import io.infinicast.client.impl.contexts.APListeningStartedContext;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.objectState.Endpoint;
import io.infinicast.client.impl.pathAccess.EndpointAndData;
import io.infinicast.client.impl.pathAccess.IEndpointAndData;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.protocol.Connector2EpsMessageType;

import java.util.ArrayList;
import java.util.function.Consumer;
public class ListenerQueryExecutor extends BaseQueryExecutor  {
    public ListenerQueryExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void getListenerList(final TriConsumer<ICError, ArrayList<IEndpointAndData>, IAPathContext> callback, String roleFilter, ListeningType listeningType) {
        JObject settings = new JObject();
        if (!(StringExtensions.IsNullOrEmpty(roleFilter))) {
            settings.set("role", roleFilter);
        }
        if (listeningType != ListeningType.Any) {
            settings.set("messageType", listeningType.toString());
        }
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetListeningList, super._path, settings, (json, err, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                callback.accept(error, null, null);
                ;
            }))) {
                JArray array = json.getJArray("list");
                if (array != null) {
                    ArrayList<IEndpointAndData> resultList = new ArrayList<IEndpointAndData>();
                    for (JToken ob : array) {
                        Endpoint endpointObject = new Endpoint(ob.getString("path"), ob.getString("endpoint"), super._connector.getRootPath());
                        EndpointAndData endpointData = new EndpointAndData();
                        if (ob.containsNonNull("data")) {
                            endpointData.setData(ob.getJObject("data"));
                        }
                        endpointData.setEndpoint(endpointObject);
                        resultList.add(endpointData);
                    }
                    callback.accept(null, resultList, super.getPathContext(super._path));
                    ;
                }
                else {
                    throw new RuntimeException(new Exception("GetListeningList should always contain a list, even if it is empty"));
                }
            }
            ;
        });
    }
    static APListeningStartedContext getListeningStartedContext(JObject json, IPathAndEndpointContext ctx) {
        APListeningStartedContext context = new APListeningStartedContext();
        if (json != null) {
            context.listenerCount = BaseQueryExecutor.getRoleCountDictionary(json);
        }
        context.setPath(ctx.getPath());
        context.setEndpoint(ctx.getEndpoint());
        context.setEndpointData(ctx.getEndpointData());
        return context;
    }
    static APListeningEndedContext getListeningEndedContext(JObject json, IPathAndEndpointContext ctx) {
        APListeningEndedContext context = new APListeningEndedContext();
        if (json != null) {
            context.listenerCount = BaseQueryExecutor.getRoleCountDictionary(json);
        }
        context.setPath(ctx.getPath());
        context.setEndpoint(ctx.getEndpoint());
        context.setEndpointData(ctx.getEndpointData());
        context.setIsDisconnected(json.getBoolean("disconnected"));
        return context;
    }
    public void onListeningStarted(Consumer<IListeningStartedContext> handler) {
        this.onListeningStarted(handler, (ListeningHandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onListeningStarted(Consumer<IListeningStartedContext> handler, ListeningHandlerRegistrationOptions options) {
        this.onListeningStarted(handler, options, (CompleteCallback) null);
    }
    JObject getCustomOptionsJson(ListeningHandlerRegistrationOptions options) {
        JObject customOptions = null;
        if ((options != null) && !(StringExtensions.IsNullOrEmpty(options.getRoleFilter()))) {
            if (customOptions == null) {
                customOptions = new JObject();
            }
            customOptions.set("role", options.getRoleFilter());
        }
        if ((options != null) && (options.getListenerType() != ListeningType.Any)) {
            if (customOptions == null) {
                customOptions = new JObject();
            }
            customOptions.set("listenerType", options.getListenerType().toString());
        }
        return customOptions;
    }
    public void onListeningStarted(Consumer<IListeningStartedContext> handler, ListeningHandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((handler == null), Connector2EpsMessageType.ListeningStarted, super._path, (json, err, ctx, id) -> {
            APListeningStartedContext context = ListenerQueryExecutor.getListeningStartedContext(json, ctx);
            //                    context.IsDisconnected = (bool)json["disconnected"];
                    handler.accept(context);
            ;
        }
        , completeCallback, options);
    }
    public void getAndListenOnListeners(final Consumer<IListeningStartedContext> onStart, final Consumer<IListeningChangedContext> onChange, final Consumer<IListeningEndedContext> onEnd, ListeningHandlerRegistrationOptions options, final CompleteCallback registrationCompleteCallback) {
        JObject parameters = this.getCustomOptionsJson(options);
        if (parameters == null) {
            parameters = new JObject();
        }
        if (((onStart == null) && (onEnd == null)) && (onChange == null)) {
            parameters.set("remove", true);
        }
        if ((options != null) && options.getIsOncePerRole()) {
            parameters.set("once", true);
        }
        if ((options != null) && options.getIsSticky()) {
            parameters.set("sticky", true);
        }
        if (onChange == null) {
            parameters.set("noChange", true);
        }
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetAndListenOnListeners, super._path, parameters, (json, err, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                if (registrationCompleteCallback != null) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                ;
            }))) {
                JArray array = json.getJArray("list");
                if (array != null) {
                    PathImpl rootPath = super._connector.getRootPath();
                    for (JToken ob : array) {
                        Endpoint endpointObject = new Endpoint(ob.getString("path"), ob.getString("endpoint"), rootPath);
                        EndpointAndData endpointData = new EndpointAndData();
                        endpointData.setData(ob.getJObject("data"));
                        endpointData.setEndpoint(endpointObject);
                        APListeningStartedContext listeningStartedContext = new APListeningStartedContext();
                        listeningStartedContext.setEndpoint(endpointObject);
                        listeningStartedContext.setEndpointData(endpointData.getData());
                        if (onStart != null) {
                            onStart.accept(listeningStartedContext);
                            ;
                        }
                    }
                }
                if (registrationCompleteCallback != null) {
                    registrationCompleteCallback.accept(null);
                    ;
                }
            }
            ;
        });
        if (((onStart != null) || (onChange != null)) || (onEnd != null)) {
            super._messageManager.registerHandler(Connector2EpsMessageType.ListeningStarted, super._path, (json, err, ctx, id) -> {
                APListeningStartedContext context = ListenerQueryExecutor.getListeningStartedContext(json, ctx);
                if (onStart != null) {
                    onStart.accept(context);
                    ;
                }
            }
            );
            if (onChange != null) {
                super._messageManager.registerHandler(Connector2EpsMessageType.ListeningChanged, super._path, (json, err, ctx, id) -> {
                    onChange.accept(ListenerQueryExecutor.getListeningChangedContext(json, ctx));
                    ;
                });
            }
            super._messageManager.registerHandler(Connector2EpsMessageType.ListeningEnded, super._path, (json, err, ctx, id) -> {
                APListeningEndedContext context = ListenerQueryExecutor.getListeningEndedContext(json, ctx);
                if (onEnd != null) {
                    onEnd.accept(context);
                    ;
                }
            }
            );
        }
        else {
            super._messageManager.registerHandler(Connector2EpsMessageType.ListeningStarted, super._path, null);
            super._messageManager.registerHandler(Connector2EpsMessageType.ListeningChanged, super._path, null);
            super._messageManager.registerHandler(Connector2EpsMessageType.ListeningEnded, super._path, null);
        }
    }
    static void forwardListeningEndedMessages(Consumer<IListeningEndedContext> handler, JObject json, IPathAndEndpointContext ctx) {
        APListeningEndedContext context = ListenerQueryExecutor.getListeningEndedContext(json, ctx);
        handler.accept(context);
        ;
    }
    static void forwardListeningChangedMessages(Consumer<IListeningChangedContext> handler, JObject json, IPathAndEndpointContext ctx) {
        APListeningChangedContext context = ListenerQueryExecutor.getListeningChangedContext(json, ctx);
        handler.accept(context);
        ;
    }
    static APListeningChangedContext getListeningChangedContext(JObject json, IPathAndEndpointContext ctx) {
        APListeningChangedContext context = new APListeningChangedContext();
        context.setPath(ctx.getPath());
        context.setEndpoint(ctx.getEndpoint());
        context.setEndpointData(ctx.getEndpointData());
        return context;
    }
    public void onListeningChanged(final Consumer<IListeningChangedContext> handler, ListeningHandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((handler == null), Connector2EpsMessageType.ListeningChanged, super._path, (json, err, ctx, id) -> {
            ListenerQueryExecutor.forwardListeningChangedMessages(handler, json, ctx);
        }
        , completeCallback, options);
    }
    public void onListeningChanged(final Consumer<IListeningChangedContext> handler, ListeningHandlerRegistrationOptions options) {
        this.onListeningChanged(handler, options, (CompleteCallback) null);
    }
    public void onListeningChanged(final Consumer<IListeningChangedContext> handler) {
        this.onListeningChanged(handler, (ListeningHandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onListeningEnded(final Consumer<IListeningEndedContext> handler, ListeningHandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((handler == null), Connector2EpsMessageType.ListeningEnded, super._path, (json, err, ctx, id) -> {
            ListenerQueryExecutor.forwardListeningEndedMessages(handler, json, ctx);
        }
        , completeCallback, options);
    }
    public void onListeningEnded(final Consumer<IListeningEndedContext> handler, ListeningHandlerRegistrationOptions options) {
        this.onListeningEnded(handler, options, (CompleteCallback) null);
    }
    public void onListeningEnded(final Consumer<IListeningEndedContext> handler) {
        this.onListeningEnded(handler, (ListeningHandlerRegistrationOptions) null, (CompleteCallback) null);
    }
}
