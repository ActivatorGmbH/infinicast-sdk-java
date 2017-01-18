package io.infinicast.client.impl.messaging;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.HandlerRegistrationOptionsData;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.query.ListenTerminateReason;
import io.infinicast.client.api.query.ListeningType;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.APathContext;
import io.infinicast.client.impl.helper.LockObject;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.impl.messaging.handlers.DMessageResponseHandler;
import io.infinicast.client.impl.messaging.receiver.ConnectorMessageReceiver;
import io.infinicast.client.impl.messaging.receiver.IMessageReceiver;
import io.infinicast.client.impl.messaging.sender.IMessageSender;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.infinicast.client.protocol.Connector2EpsProtocol;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
public class ConnectorMessageManager implements IEndpoint2ServerNetLayerHandler {
    Connector2EpsProtocol _connector2EpsProtocol = new Connector2EpsProtocol();
    IMessageReceiver _receiver = new ConnectorMessageReceiver();
    IConnector _connector;
    int _requestId = 1;
    IMessageSender _sender;
    LockObject requestIdLock = new LockObject();
    public void onConnect() {
        this.sendInitMessage(this._connector.getSpace(), this._connector.getRole(), this._connector.getCredentials());
    }
    public void onReceiveFromServer(APlayStringMessage stringMessage) {
        this.getReceiver().receive(stringMessage);
    }
    public void onDisconnect() {
        this._connector.triggerDisconnect();
    }
    public void onPing(int msgLastRoundTrip, long msgSendTime) {
        this._connector.receivedPing(msgLastRoundTrip, msgSendTime);
    }
    public void setSender(IMessageSender sender) {
        this._sender = sender;
    }
    public void sendMessageWithResponseString(Connector2EpsMessageType messageType, String pathString, JObject data, final DMessageResponseHandler responseHandler) {
        int messageRequestId = this.getRequestId();
        this._receiver.addResponseHandler(Connector2EpsMessageType.RequestResponse, String.valueOf(messageRequestId), (json, error, context, requestedId) -> {
            responseHandler.accept(json, error, context);
            ;
        });
        this._sender.sendMessage(this._connector2EpsProtocol.encodeMessageWithResponse(messageType, pathString, data, messageRequestId));
    }
    public void sendMessageWithResponse(Connector2EpsMessageType messageType, IPath path, JObject data, DMessageResponseHandler responseHandler) {
        String strPath = "";
        if (path != null) {
            strPath = path.toString();
        }
        this.sendMessageWithResponseString(messageType, strPath, data, responseHandler);
    }
    int getRequestId() {
        int id;
        synchronized (this.requestIdLock) {
            id = ++(this._requestId);
        }
        return id;
    }
    public void sendInitMessage(String space, String type, JObject credentials) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeInitConnector(space, type, credentials));
    }
    public void sendRequestAnswer(Connector2EpsMessageType messageType, IPath path, JObject data, String targetEndpoint, int requestId) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeMessageWithRequestId(messageType, path.toString(), data, targetEndpoint, requestId));
    }
    public void sendMessageString(Connector2EpsMessageType messageType, String pathStr, JObject data) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeMessage(messageType, pathStr, data));
    }
    public void sendMessage(Connector2EpsMessageType messageType, IPath path, JObject data) {
        this.sendMessageString(messageType, path.toString(), data);
    }
    public void sendValidatedMessage(Connector2EpsMessageType messageType, IPath path, JObject data, String originalEndpoint) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeValidatedMessage(messageType, path.toString(), data, originalEndpoint));
    }
    public void addHandler(boolean isDelete, Connector2EpsMessageType messageType, final IPath path, DCloudMessageHandler handler, final CompleteCallback completeCallback, HandlerRegistrationOptionsData options, final BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler) {
        Boolean consomeOnePerRole = null;
        if (options != null) {
            consomeOnePerRole = options.getIsOncePerRole();
        }
        Boolean sticky = null;
        if ((options != null) && options.getIsSticky()) {
            sticky = true;
        }
        boolean terminationHandler = (listenTerminationHandler != null);
        ListeningType listeningType = ListeningType.Any;
        if (options != null) {
            listeningType = options.getListenerType();
        }
        String roleFilter = "";
        if ((options != null) && !(StringExtensions.IsNullOrEmpty(options.getRoleFilter()))) {
            roleFilter = options.getRoleFilter();
        }
        int messageRequestId = this.getRequestId();
        this._receiver.addResponseHandler(Connector2EpsMessageType.RequestResponse, String.valueOf(messageRequestId), (json, error, context, requestedId) -> {
            if (error != null) {
                if (completeCallback != null) {
                    completeCallback.accept(error);
                    ;
                }
                else {
                    this.getConnector().unhandeledErrorInfo(path, error);
                }
            }
            else {
                if (completeCallback != null) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        });
        if (!(isDelete)) {
            this._sender.sendMessage(this._connector2EpsProtocol.encodeRegisterHandlerMessage(messageType, path.toString(), messageRequestId, consomeOnePerRole, sticky, listeningType, roleFilter, terminationHandler));
            this._receiver.addHandler(messageType.toString(), path, handler);
            if (listenTerminationHandler != null) {
                this._receiver.addHandler((messageType.toString() + "_ListenTerminate"), path, (json, error, context, id) -> {
                    Console.WriteLine("Listenterminate received " + json.toString());
                    APathContext ctx = new APathContext();
                    ctx.setPath(context.getPath());
                    ListenTerminateReason reason = (ListenTerminateReason) ListenTerminateReason.valueOf(json.getString("reason"));
                    listenTerminationHandler.accept(reason, ctx);
                    ;
                });
            }
        }
        else {
            this._sender.sendMessage(this._connector2EpsProtocol.encodeRemoveHandlerMessage(path.toString(), messageType, messageRequestId, ""));
            this._receiver.removeHandlers(messageType.toString(), path.toString());
            this._receiver.removeHandlers((messageType.toString() + "_ListenTerminate"), path.toString());
        }
    }
    public void registerHandler(Connector2EpsMessageType messageType, IPath path, DCloudMessageHandler handler) {
        if (handler != null) {
            this._receiver.addHandler(messageType.toString(), path, handler);
        }
        else {
            this._receiver.removeHandlers(messageType.toString(), path.toString());
        }
    }
    public IMessageReceiver getReceiver() {
        return this._receiver;
    }
    public void setConnector(IConnector connector) {
        this._connector = connector;
        this.getReceiver().setConnector(connector);
    }
    public IConnector getConnector() {
        return this._connector;
    }
    public void sendDebugPingInfo(IPath iaPath, int pingInMs) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeDebugPingInfo(iaPath.toString(), pingInMs));
    }
    public void sendDebugMessage(IPath iaPath, int level, JObject data) {
    }
    public void sendUpdateDebugStatistics(JObject filters, final Consumer<JObject> handler) {
        int messageRequestId = this.getRequestId();
        this._receiver.addResponseHandler(Connector2EpsMessageType.RequestResponse, String.valueOf(messageRequestId), (json, error, context, requestedId) -> {
            handler.accept(json);
        });
        this._sender.sendMessage(this._connector2EpsProtocol.encodeMessageWithResponse(Connector2EpsMessageType.DebugStatistics, "", filters, messageRequestId));
    }
    public void destroy() {
        this._receiver.destroy();
        this._sender.destroy();
    }
    public void addHandler(boolean isDelete, Connector2EpsMessageType messageType, final IPath path, DCloudMessageHandler handler, final CompleteCallback completeCallback, HandlerRegistrationOptionsData options) {
        this.addHandler(isDelete, messageType, path, handler, completeCallback, options, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
}
