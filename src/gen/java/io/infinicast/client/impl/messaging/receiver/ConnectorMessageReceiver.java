package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.PathAndEndpointContext;
import io.infinicast.client.impl.messaging.PathHandlerContainer;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.impl.objectState.Endpoint;
import io.infinicast.client.impl.objectState.ObjectStateManager;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.infinicast.client.protocol.Eps2ConnectorProtocol;
import io.infinicast.client.protocol.IEndpoint2ConnectorProtocolHandler;
import io.infinicast.client.utils.PathUtils;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
public class ConnectorMessageReceiver implements IMessageReceiver, IEndpoint2ConnectorProtocolHandler {
    ConcurrentHashMap<String, PathHandlerContainer> _handlerMap = new ConcurrentHashMap<String, PathHandlerContainer>();
    Logger _logger = LoggerFactory.getLogger(ConnectorMessageReceiver.class);
    Eps2ConnectorProtocol _receiveProtocol = new Eps2ConnectorProtocol();
    HandlerPool handlerPool = new HandlerPool();
    IConnector _connector;
    public void onInitConnector(JObject data, JObject senderEndpoint) {
        this._connector.onInitConnector(data, senderEndpoint);
    }
    public void onReceiveRequestResponse(JObject data, int requestId, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, "");
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, endpointOb, requestId, 1);
    }
    public void onReceiveRequest(JObject data, String path, int requestId, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.Request, data, endpointOb, requestId);
    }
    public void onReceiveJsonQueryResult(JArray list, int fullCount, int requestId) {
        JObject data = new JObject();
        data.set("list", list);
        data.set("fullCount", fullCount);
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, new PathAndEndpointContext(null, null, null), requestId, 1);
    }
    public void onCreateChildSuccess(JObject data, String path, int requestId) {
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onGetOrCreate(JObject data, String path, int requestId, boolean newlyCreated) {
        JObject parameters = new JObject();
        parameters.set("data", data);
        parameters.set("newlyCreated", newlyCreated);
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, parameters, this.getEndpointContext(null, path), requestId, 1);
    }
    public void onCreateOrUpdateRole(JObject data, int requestId) {
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onDestroyRole(JObject data, int requestId) {
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onGetRoleForPathResult(JArray list, JObject data, int requestId) {
        if ((data == null)) {
            data = new JObject();
        }
        data.set("list", list);
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onIntroduceObject(JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(PathUtils.getObjectListPath(path), Connector2EpsMessageType.IntroduceObject, data, endpointOb, 0);
    }
    public void onListeningEnded(String path, JObject endpointObject, boolean disconnected, JObject data) {
        data.set("disconnected", disconnected);
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.ListeningEnded, data, endpointOb, 0);
    }
    public void onListeningStarted(String path, JObject endpointObject, JObject data) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.ListeningStarted, data, endpointOb, 0);
    }
    public void onListeningChanged(String path, JObject endpointObject, JObject data) {
        data.set("path", PathUtils.getEndpointPath(data.getString("endpoint")));
        PathAndEndpointContext endpointOb = this.getEndpointContext(data, path);
        this.callHandlers(path, Connector2EpsMessageType.ListeningChanged, data, endpointOb, 0);
    }
    public void onReceiveMessage(JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.Message, data, endpointOb, 0);
    }
    public void onReceiveMessageValidate(JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.MessageValidate, data, endpointOb, 0);
    }
    public void onReceiveDataChangeValidate(JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.DataChangeValidate, data, endpointOb, 0);
    }
    public void onListAdd(JObject data, String listPath, String path, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(listPath, Connector2EpsMessageType.ListAdd, data, endpointOb, 0);
    }
    public void onListChange(JObject data, String listPath, String path, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(listPath, Connector2EpsMessageType.ListChange, data, endpointOb, 0);
    }
    public void onListRemove(JObject data, String listPath, String path, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(listPath, Connector2EpsMessageType.ListRemove, data, endpointOb, 0);
    }
    public void onSetObjectData(JObject data, String path, JObject endpointObject) {
        ObjectStateManager objManager = this._connector.getObjectStateManager();
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.SetObjectData, data, endpointOb, 0);
    }
    public void onDebugStatistics(JObject json, int requestId) {
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, new JObject(json), this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onPathRoleSetup(JObject data, int requestId) {
        this.callHandlersLimited("", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onReminderTriggered(String path, JObject data) {
        this.callHandlers(path, Connector2EpsMessageType.Reminder, data, null, 0);
    }
    public void onListenTerminate(JObject data) {
        JArray paths = data.getJArray("paths");
        String handlerType = data.getString("handlerType");
        if (!(StringExtensions.IsNullOrEmpty(handlerType))) {
            this.callListenTerminate(data, paths, handlerType);
        }
        else {
            this.callListenTerminate(data, paths, "Message");
            this.callListenTerminate(data, paths, "MessageValidate");
            this.callListenTerminate(data, paths, "Request");
            this.callListenTerminate(data, paths, "SetObjectData");
            this.callListenTerminate(data, paths, "SetObjectDataValidate");
        }
    }
    public void onEndpointDisconnected(String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(path, Connector2EpsMessageType.EndpointDisconnected, null, endpointOb, 0);
    }
    public void onDebugObserverMessage(String path, JObject data) {
        this.callHandlers(path, Connector2EpsMessageType.DebugObserverMessage, data, null, 0);
    }
    void callListenTerminate(JObject data, JArray paths, String handlerType) {
        for (JToken path : paths) {
            String pathString = path.toString();
            this.callHandlersByString(pathString, ((handlerType + "_") + Connector2EpsMessageType.ListenTerminate.toString()), data, this.getEndpointContext(null, pathString), 0);
        }
    }
    public void addHandler(String messageType, IPath path, DCloudMessageHandler handler) {
        PathHandlerContainer messageHandlerBag = this.ensureMessageHandlerBag(messageType, path);
        messageHandlerBag.addHandler(messageType, handler);
    }
    public void removeHandlers(String messageType, String path) {
        if (this._handlerMap.containsKey(path)) {
            PathHandlerContainer bag = this._handlerMap.get(path);
            if ((bag != null)) {
                bag.removeHandler(messageType);
                if (bag.isEmpty()) {
                    this._handlerMap.remove(path);
                }
            }
        }
    }
    public void addResponseHandler(Connector2EpsMessageType messageType, String requestId, DCloudMessageHandler handler) {
        String messageTypeAsString = messageType.toString();
        PathHandlerContainer messageHandlerBag = this.ensureMessageHandlerBag(((messageTypeAsString + "_") + requestId), null);
        messageHandlerBag.addHandler(messageTypeAsString, handler);
    }
    public void receive(APlayStringMessage msg) {
        if (this._logger.getIsDebugEnabled()) {
            this._logger.debug(("received " + msg.getDataAsString()));
        }
        try {
            this._receiveProtocol.decodeStringMessage(msg, this);
        }
        catch (Exception ex) {
            this._logger.error(((("Exception in decode message " + msg.getDataAsString()) + " ") + ex.getMessage()));
        }
    }
    public void setConnector(IConnector connector) {
        this._connector = connector;
    }
    PathHandlerContainer ensureMessageHandlerBag(String name, IPath path) {
        if ((path != null)) {
            name = path.toString();
        }
        PathHandlerContainer bag = new PathHandlerContainer(path);
        bag = ConcurrentHashmapExtensions.getOrAdd(name, bag, this._handlerMap);
        return bag;
    }
    ArrayList<PathHandlerContainer> getMessageHandlerBags(String name, String path) {
        ArrayList<PathHandlerContainer> bags = new ArrayList<PathHandlerContainer>();
        for (String p : PathUtils.getWildCardedPaths(path)) {
            String n = name;
            if (!(StringExtensions.IsNullOrEmpty(p))) {
                n = p;
            }
            PathHandlerContainer bag = this._handlerMap.get(n);
            if ((bag != null)) {
                bags.add(bag);
            }
        }
        return bags;
    }
    void callHandlersLimitedByString(String path, String type, JObject data, IPathAndEndpointContext context, int requestId, int handlerCount) {
        int callCount = 0;
        ArrayList<PathHandlerContainer> bags;
        if (((requestId != 0) && StringExtensions.IsNullOrEmpty(path))) {
            bags = this.getMessageHandlerBags(((type + "_") + requestId), "");
            this._handlerMap.remove(((type + "_") + requestId));
        }
        else {
            bags = this.getMessageHandlerBags(type.toString(), path);
        }
        for (PathHandlerContainer bag : bags) {
            if (((handlerCount == 0) || (callCount < handlerCount))) {
                callCount++;
                try {
                    this.queueInHandlerPool(bag, type, data, context, requestId);
                }
                catch (Exception ex) {
                    ErrorInfo errorInfo = ErrorInfo.fromMessage(StringExtensions.formatException(ex), path);
                    this._connector.unhandeledErrorInfo(null, errorInfo);
                }
            }
        }
        if ((bags.size() == 0)) {
            this._logger.warn((((("request without handler " + path) + " '") + type) + "'"));
        }
    }
    void queueInHandlerPool(final PathHandlerContainer bag, final String type, final JObject data, final IPathAndEndpointContext context, final int requestId) {
        ConnectorMessageReceiver self = this;
        this.handlerPool.queueHandlerCall(new Action() {
            public void accept() {
                bag.callHandlers(type, data, context, requestId);
            }
        }
        );
    }
    void callHandlersLimited(String path, Connector2EpsMessageType type, JObject data, IPathAndEndpointContext context, int requestId, int handlerCount) {
        this.callHandlersLimitedByString(path, type.toString(), data, context, requestId, handlerCount);
    }
    void callHandlers(String path, Connector2EpsMessageType type, JObject data, IPathAndEndpointContext context, int requestId) {
        this.callHandlersLimited(path, type, data, context, requestId, 0);
    }
    void callHandlersByString(String path, String type, JObject data, IPathAndEndpointContext context, int requestId) {
        this.callHandlersLimitedByString(path, type, data, context, requestId, 0);
    }
    PathAndEndpointContext getEndpointContext(JObject senderEndpointObject, String pathStr) {
        Endpoint endpoint = null;
        if ((senderEndpointObject != null)) {
            endpoint = new Endpoint(senderEndpointObject.getString("path"), senderEndpointObject.getString("endpoint"), this._connector.getRootPath());
        }
        JObject endpointData = null;
        if (((senderEndpointObject != null) && (senderEndpointObject.get("data") != null))) {
            endpointData = senderEndpointObject.getJObject("data");
        }
        IPath path = null;
        if (!(StringExtensions.IsNullOrEmpty(pathStr))) {
            path = this.getConnector().path(pathStr);
        }
        return new PathAndEndpointContext(path, endpoint, endpointData);
    }
    public IConnector getConnector() {
        return this._connector;
    }
}
