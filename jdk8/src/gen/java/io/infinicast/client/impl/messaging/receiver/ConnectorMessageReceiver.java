package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.PathAndEndpointContext;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
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
    ConnectorMessageManager _connectorMessageManager;
    public ConnectorMessageReceiver(ConnectorMessageManager connectorMessageManager) {
        this._connectorMessageManager = connectorMessageManager;
    }
    public void onInitConnector(ICError error, JObject data, JObject senderEndpoint) {
        this._connector.onInitConnector(error, data, senderEndpoint);
    }
    public void onReceiveRequestResponse(ICError error, JObject data, int requestId, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, "");
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, data, endpointOb, requestId, 1);
    }
    public void onReceiveRequest(ICError error, JObject data, String path, int requestId, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        boolean wasSomethingCalled = this.callHandlers(error, path, Connector2EpsMessageType.Request, data, endpointOb, requestId);
        String endpointId = endpointOb.getEndpoint().getEndpointId();
        if (!(wasSomethingCalled)) {
            this._logger.error(((("did not find a handler for the request " + path) + " ") + endpointId) + requestId);
            this._connectorMessageManager.sendRequestAnswerString(Connector2EpsMessageType.RequestHandlingFailed, path, null, endpointId, requestId);
        }
    }
    public void onReceiveJsonQueryResult(ICError error, JArray list, int fullCount, int requestId) {
        JObject data = new JObject();
        data.set("list", list);
        data.set("fullCount", fullCount);
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, data, new PathAndEndpointContext(null, null, null), requestId, 1);
    }
    public void onCreateChildSuccess(ICError error, JObject data, String path, int requestId) {
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onGetOrCreate(ICError error, JObject data, String path, int requestId, boolean newlyCreated) {
        JObject parameters = new JObject();
        parameters.set("data", data);
        parameters.set("newlyCreated", newlyCreated);
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, parameters, this.getEndpointContext(null, path), requestId, 1);
    }
    public void onCreateOrUpdateRole(ICError error, JObject data, int requestId) {
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onDestroyRole(ICError error, JObject data, int requestId) {
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onGetRoleForPathResult(ICError error, JArray list, JObject data, int requestId) {
        JObject safeData = data;
        if (null == safeData) {
            safeData = new JObject();
        }
        safeData.set("list", list);
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, safeData, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onIntroduceObject(ICError error, JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, PathUtils.getObjectListPath(path), Connector2EpsMessageType.IntroduceObject, data, endpointOb, 0);
    }
    public void onListeningEnded(ICError error, String path, JObject endpointObject, boolean disconnected, JObject data) {
        data.set("disconnected", disconnected);
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.ListeningEnded, data, endpointOb, 0);
    }
    public void onListeningStarted(ICError error, String path, JObject endpointObject, JObject data) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.ListeningStarted, data, endpointOb, 0);
    }
    public void onListeningChanged(ICError error, String path, JObject endpointObject, JObject data) {
        data.set("path", PathUtils.getEndpointPath(data.getString("endpoint")));
        PathAndEndpointContext endpointOb = this.getEndpointContext(data, path);
        this.callHandlers(error, path, Connector2EpsMessageType.ListeningChanged, data, endpointOb, 0);
    }
    public void onReceiveMessage(ICError error, JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.Message, data, endpointOb, 0);
    }
    public void onReceiveMessageValidate(ICError error, JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.MessageValidate, data, endpointOb, 0);
    }
    public void onReceiveDataChangeValidate(ICError error, JObject data, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.DataChangeValidate, data, endpointOb, 0);
    }
    public void onListAdd(ICError error, JObject data, String listPath, String path, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(error, listPath, Connector2EpsMessageType.ListAdd, data, endpointOb, 0);
    }
    public void onListChange(ICError error, JObject data, String listPath, String path, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(error, listPath, Connector2EpsMessageType.ListChange, data, endpointOb, 0);
    }
    public void onListRemove(ICError error, JObject data, String listPath, String path, JObject senderEndpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(senderEndpointObject, path);
        this.callHandlers(error, listPath, Connector2EpsMessageType.ListRemove, data, endpointOb, 0);
    }
    public void onSetObjectData(ICError error, JObject data, String path, JObject endpointObject) {
        ObjectStateManager objManager = this._connector.getObjectStateManager();
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.SetObjectData, data, endpointOb, 0);
    }
    public void onDebugStatistics(ICError error, JObject json, int requestId) {
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, new JObject(json), this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onPathRoleSetup(ICError error, JObject data, int requestId) {
        this.callHandlersLimited(error, "", Connector2EpsMessageType.RequestResponse, data, this.getEndpointContext(null, ""), requestId, 1);
    }
    public void onReminderTriggered(ICError error, String path, JObject data) {
        this.callHandlers(error, path, Connector2EpsMessageType.Reminder, data, null, 0);
    }
    public void onListenTerminate(ICError error, JObject data) {
        JArray paths = data.getJArray("paths");
        String handlerType = data.getString("handlerType");
        if (!(StringExtensions.IsNullOrEmpty(handlerType))) {
            this.callListenTerminate(error, data, paths, handlerType);
        }
        else {
            this.callListenTerminate(error, data, paths, "Message");
            this.callListenTerminate(error, data, paths, "MessageValidate");
            this.callListenTerminate(error, data, paths, "Request");
            this.callListenTerminate(error, data, paths, "SetObjectData");
            this.callListenTerminate(error, data, paths, "SetObjectDataValidate");
        }
    }
    public void onEndpointDisconnected(ICError error, String path, JObject endpointObject) {
        PathAndEndpointContext endpointOb = this.getEndpointContext(endpointObject, path);
        this.callHandlers(error, path, Connector2EpsMessageType.EndpointDisconnected, null, endpointOb, 0);
    }
    public void onDebugObserverMessage(ICError error, String path, JObject data) {
        this.callHandlers(error, path, Connector2EpsMessageType.DebugObserverMessage, data, null, 0);
    }
    void callListenTerminate(ICError error, JObject data, JArray paths, String handlerType) {
        for (JToken path : paths) {
            String pathString = path.toString();
            this.callHandlersByString(error, pathString, ((handlerType + "_") + Connector2EpsMessageType.ListenTerminate.toString()), data, this.getEndpointContext(null, pathString), 0);
        }
    }
    public void addHandler(String messageType, IPath path, DCloudMessageHandler handler) {
        PathHandlerContainer messageHandlerBag = this.ensureMessageHandlerBag(messageType, path);
        messageHandlerBag.addHandler(messageType, handler);
    }
    public boolean removeHandlers(String messageType, String path) {
        boolean found = false;
        if (this._handlerMap.containsKey(path)) {
            PathHandlerContainer bag = this._handlerMap.get(path);
            if (bag != null) {
                if (bag.removeHandler(messageType)) {
                    found = true;
                }
                if (bag.isEmpty()) {
                    this._handlerMap.remove(path);
                }
            }
        }
        return found;
    }
    public void destroy() {
        this._handlerMap.clear();
        this.handlerPool.Destroy();
    }
    public void addResponseHandler(Connector2EpsMessageType messageType, String requestId, DCloudMessageHandler handler) {
        String messageTypeAsString = messageType.toString();
        PathHandlerContainer messageHandlerBag = this.ensureMessageHandlerBag(((messageTypeAsString + "_") + requestId), null);
        messageHandlerBag.addHandler(messageTypeAsString, handler);
    }
    public void receive(APlayStringMessage msg) {
        if (this._logger.getIsDebugEnabled()) {
            this._logger.debug("received " + msg.getDataAsString());
        }
        try {
            this._receiveProtocol.decodeStringMessage(msg, this);
        }
        catch (Exception ex) {
            this._logger.error((("Exception in decode message " + msg.getDataAsString()) + " ") + InfinicastExceptionHelper.ExceptionToString(ex));
        }
    }
    public void setConnector(IConnector connector) {
        this._connector = connector;
    }
    PathHandlerContainer ensureMessageHandlerBag(String name, IPath path) {
        String safeName = name;
        if (path != null) {
            safeName = path.toString();
        }
        PathHandlerContainer bag = new PathHandlerContainer(path);
        bag = ConcurrentHashmapExtensions.getOrAdd(this._handlerMap, safeName, bag);
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
            if (bag != null) {
                bags.add(bag);
            }
        }
        return bags;
    }
    int callHandlersLimitedByString(ICError error, String path, String type, JObject data, IPathAndEndpointContext context, int requestId, int handlerCount) {
        int callCount = 0;
        ArrayList<PathHandlerContainer> bags;
        if ((requestId != 0) && StringExtensions.IsNullOrEmpty(path)) {
            bags = this.getMessageHandlerBags(((type + "_") + requestId), "");
            this._handlerMap.remove((type + "_") + requestId);
        }
        else {
            bags = this.getMessageHandlerBags(type.toString(), path);
        }
        for (PathHandlerContainer bag : bags) {
            if ((handlerCount == 0) || (callCount < handlerCount)) {
                try {
                    this.queueInHandlerPool(error, bag, type, data, context, requestId);
                    (callCount)++;
                }
                catch (Exception ex) {
                    ICError icError = ICError.fromException(ex, path);
                    this._connector.unhandeledErrorInfo(null, icError);
                }
            }
        }
        if (callCount == 0) {
            this._logger.warn((((("request without handler " + path) + " '") + type) + "'") + callCount);
        }
        return callCount;
    }
    void queueInHandlerPool(final ICError error, final PathHandlerContainer bag, final String type, final JObject data, final IPathAndEndpointContext context, final int requestId) {
        this.handlerPool.QueueHandlerCall(() -> {
            bag.callHandlers(error, type, data, context, requestId);
        });
    }
    int callHandlersLimited(ICError error, String path, Connector2EpsMessageType type, JObject data, IPathAndEndpointContext context, int requestId, int handlerCount) {
        return this.callHandlersLimitedByString(error, path, type.toString(), data, context, requestId, handlerCount);
    }
    boolean callHandlers(ICError error, String path, Connector2EpsMessageType type, JObject data, IPathAndEndpointContext context, int requestId) {
        return (this.callHandlersLimited(error, path, type, data, context, requestId, 0) > 0);
    }
    void callHandlersByString(ICError error, String path, String type, JObject data, IPathAndEndpointContext context, int requestId) {
        this.callHandlersLimitedByString(error, path, type, data, context, requestId, 0);
    }
    PathAndEndpointContext getEndpointContext(JObject senderEndpointObject, String pathStr) {
        Endpoint endpoint = null;
        if (senderEndpointObject != null) {
            endpoint = new Endpoint(senderEndpointObject.getString("path"), senderEndpointObject.getString("endpoint"), this._connector.getRootPath());
        }
        JObject endpointData = null;
        if ((senderEndpointObject != null) && (senderEndpointObject.get("data") != null)) {
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
