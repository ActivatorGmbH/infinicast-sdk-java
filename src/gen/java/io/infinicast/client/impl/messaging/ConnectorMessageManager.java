package io.infinicast.client.impl.messaging;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.HandlerRegistrationOptionsData;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.SetDataDiagnosticOrigin;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.query.ListenTerminateReason;
import io.infinicast.client.api.query.ListeningType;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.InfinicastClient;
import io.infinicast.client.impl.contexts.APathContext;
import io.infinicast.client.impl.helper.LockObject;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.impl.messaging.handlers.DMessageResponseHandler;
import io.infinicast.client.impl.messaging.receiver.ConnectorMessageReceiver;
import io.infinicast.client.impl.messaging.receiver.IMessageReceiver;
import io.infinicast.client.impl.messaging.sender.IMessageSender;
import io.infinicast.client.impl.messaging.sender.MessageSender;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.infinicast.client.protocol.Connector2EpsProtocol;
import io.infinicast.client.protocol.messages.Connector2EpsMessage;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
public class ConnectorMessageManager implements IEndpoint2ServerNetLayerHandler {
    Connector2EpsProtocol _connector2EpsProtocol = new Connector2EpsProtocol();
    IMessageReceiver _receiver;
    IConnector _connector;
    int _requestId = 1;
    IMessageSender _sender;
    LockObject requestIdLock = new LockObject();
    private final Object diagnosticTransportLock = new Object();
    private final ThreadLocal<Long> receivingTransportGeneration = new ThreadLocal<>();
    private long activeTransportGeneration;
    private boolean authJoinMarkerV1Enabled;
    private boolean initResolvedForTransport;
    private boolean retiredTransport;
    private static final IMessageSender CLOSED_SENDER = new IMessageSender() {
        @Override public void sendMessage(Connector2EpsMessage message) {
            throw new IllegalStateException("Transport disconnected");
        }
        @Override public void destroy() { }
    };
    private static final SecureRandom JOIN_RANDOM = new SecureRandom();
    private static final char[] HEX = "0123456789abcdef".toCharArray();
    public ConnectorMessageManager() {
        this._receiver = new ConnectorMessageReceiver(this);
    }
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
        synchronized (transportLifecycleLock()) {
            if (_connector instanceof InfinicastClient)
                ((InfinicastClient) _connector).awaitInitCallback();
            synchronized (diagnosticTransportLock) {
                if (retiredTransport) throw new IllegalStateException("Transport disconnected");
                this._sender = sender;
                activeTransportGeneration++;
                authJoinMarkerV1Enabled = false;
                initResolvedForTransport = false;
            }
        }
    }
    /** Bind transport callbacks to the sender installation that created them. */
    public IEndpoint2ServerNetLayerHandler bindSender(IMessageSender sender) {
        synchronized (transportLifecycleLock()) {
            if (_connector instanceof InfinicastClient)
                ((InfinicastClient) _connector).awaitInitCallback();
            synchronized (diagnosticTransportLock) {
                setSender(sender);
                return new BoundTransportHandler(activeTransportGeneration);
            }
        }
    }
    private Object transportLifecycleLock() {
        IConnector connector = _connector;
        return connector instanceof InfinicastClient
                ? ((InfinicastClient) connector).transportLifecycleLock()
                : diagnosticTransportLock;
    }
    /** Null means a direct receiver call, outside a bound transport callback. */
    public Long receivingTransportGeneration() {
        return receivingTransportGeneration.get();
    }
    public boolean isTransportGenerationCurrent(Long generation) {
        if (generation == null) return true;
        synchronized (diagnosticTransportLock) {
            return !retiredTransport && generation == activeTransportGeneration;
        }
    }
    private final class BoundTransportHandler implements IEndpoint2ServerNetLayerHandler {
        private final long generation;
        private BoundTransportHandler(long generation) { this.generation = generation; }
        @Override public void onConnect() {
            IMessageSender boundSender;
            String space;
            String role;
            JObject credentials;
            synchronized (transportLifecycleLock()) {
                synchronized (diagnosticTransportLock) {
                    if (generation != activeTransportGeneration || retiredTransport) return;
                    boundSender = _sender;
                    space = _connector.getSpace();
                    role = _connector.getRole();
                    credentials = _connector.getCredentials();
                }
            }
            // If a replacement binds now, the old connect event can only send
            // its Init frame on the captured old sender, never the new one.
            boundSender.sendMessage(_connector2EpsProtocol.encodeInitConnector(
                    space, role, credentials));
        }
        @Override public void onReceiveFromServer(APlayStringMessage message) {
            if (!isCurrent()) { return; }
            Long priorGeneration = receivingTransportGeneration.get();
            receivingTransportGeneration.set(generation);
            try { ConnectorMessageManager.this.onReceiveFromServer(message); }
            finally {
                if (priorGeneration == null) receivingTransportGeneration.remove();
                else receivingTransportGeneration.set(priorGeneration);
            }
        }
        @Override public void onDisconnect() {
            Runnable cleanup = null;
            synchronized (transportLifecycleLock()) {
                synchronized (diagnosticTransportLock) {
                    if (generation != activeTransportGeneration || retiredTransport) { return; }
                    authJoinMarkerV1Enabled = false;
                    initResolvedForTransport = true;
                    if (_connector instanceof InfinicastClient) {
                        // Detach the matching client state atomically with the generation
                        // decision. Closing TCP is deferred outside both locks.
                        cleanup = ((InfinicastClient) _connector)
                                .detachForTransportDisconnect(ConnectorMessageManager.this);
                    } else {
                        // Non-client connectors are internal test/legacy implementations.
                        // Keep their callback serialized with the replacement bind.
                        ConnectorMessageManager.this.onDisconnect();
                    }
                }
            }
            if (cleanup != null) cleanup.run();
        }
        private boolean isCurrent() {
            synchronized (diagnosticTransportLock) {
                return !retiredTransport && generation == activeTransportGeneration;
            }
        }
    }
    /** Called only while parsing the InitConnector reply of a bound transport. */
    public void dispatchInitConnector(ICError error, JObject data, JObject endpoint) {
        Runnable clientCallback = null;
        InfinicastClient client = null;
        synchronized (transportLifecycleLock()) {
            if (_connector instanceof InfinicastClient)
                ((InfinicastClient) _connector).awaitInitCallback();
            synchronized (diagnosticTransportLock) {
                Long receiving = receivingTransportGeneration.get();
                if (retiredTransport || receiving == null ||
                        receiving != activeTransportGeneration) { return; }
                if (initResolvedForTransport) {
                    authJoinMarkerV1Enabled = false;
                    return;
                }
                initResolvedForTransport = true;
                authJoinMarkerV1Enabled = error == null && data != null &&
                        data.get("error") == null &&
                        Boolean.TRUE.equals(data.get("authJoinMarkerV1Enabled")) &&
                        endpoint != null && endpoint.getString("endpoint") != null &&
                        _connector != null && "svc.gameRoom".equals(_connector.getRole());
                if (_connector instanceof InfinicastClient) {
                    client = (InfinicastClient) _connector;
                    clientCallback = client.prepareInitConnector(error, data, endpoint);
                    if (clientCallback != null) client.beginInitCallback();
                } else {
                    // Other internal connectors retain their existing callback path.
                    _connector.onInitConnector(error, data, endpoint);
                }
            }
        }
        if (clientCallback != null) {
            try { clientCallback.run(); }
            finally { client.endInitCallback(); }
        }
    }
    /** Retire a disconnected manager, including PathImpl references kept by callers. */
    public void retireTransport() {
        synchronized (diagnosticTransportLock) {
            retiredTransport = true;
            activeTransportGeneration++;
            authJoinMarkerV1Enabled = false;
            initResolvedForTransport = true;
        }
    }
    private static String newJoinMarker() {
        byte[] bytes = new byte[16];
        JOIN_RANDOM.nextBytes(bytes);
        char[] value = new char[32];
        for (int i = 0; i < bytes.length; i++) {
            value[i * 2] = HEX[(bytes[i] >>> 4) & 15];
            value[i * 2 + 1] = HEX[bytes[i] & 15];
        }
        return new String(value);
    }
    /** Observer sees a prepared frame, not proof that the server received it. */
    public void sendObservedSetData(IPath path, JObject data, SetDataDiagnosticOrigin origin,
            CompletableFuture<Void> originalFuture,
            BiConsumer<String, CompletableFuture<Void>> preparedObserver,
            DMessageResponseHandler responseHandler) {
        String marker = null;
        Connector2EpsMessage frame = null;
        IMessageSender preparedSender = null;
        long preparedGeneration = 0;
        String responseKey = null;
        boolean disconnected;
        synchronized (diagnosticTransportLock) {
            disconnected = retiredTransport;
            if (!disconnected) {
                marker = authJoinMarkerV1Enabled &&
                        (origin == SetDataDiagnosticOrigin.AUTH_NORMAL ||
                         origin == SetDataDiagnosticOrigin.AUTH_BAN) ? newJoinMarker() : null;
                int messageRequestId = getRequestId();
                responseKey = Connector2EpsMessageType.RequestResponse + "_" + messageRequestId;
                frame = this._connector2EpsProtocol.encodeMessageWithResponse(
                        Connector2EpsMessageType.SetObjectData, path.toString(), data,
                        messageRequestId, authJoinMarkerV1Enabled ? origin : null, marker);
                this._receiver.addResponseHandler(Connector2EpsMessageType.RequestResponse,
                        String.valueOf(messageRequestId), (json, error, context, requestedId) ->
                                responseHandler.accept(json, error, context));
                preparedSender = this._sender;
                preparedGeneration = activeTransportGeneration;
            }
        }
        if (disconnected) {
            originalFuture.completeExceptionally(new IllegalStateException("Transport disconnected"));
            return;
        }
        // Run optional application code outside the lifecycle guard. Its key and
        // Future still refer to this captured frame, before any fast response.
        if (preparedObserver != null) {
            try { preparedObserver.accept(marker, originalFuture); }
            catch (RuntimeException ignored) { /* optional observer cannot alter send semantics */ }
        }
        boolean changed;
        synchronized (diagnosticTransportLock) {
            changed = retiredTransport || preparedGeneration != activeTransportGeneration ||
                    preparedSender != _sender;
            if (changed) {
                // Remove the request handler before completing the Future. An inline
                // completion callback may acquire the client lifecycle lock.
                this._receiver.removeHandlers(Connector2EpsMessageType.RequestResponse.toString(),
                        responseKey);
            }
        }
        if (changed) {
            originalFuture.completeExceptionally(new IllegalStateException("Transport changed before send"));
            return;
        }
        try {
            // A send already admitted here is bound to the captured sender. A
            // concurrent retirement can close that sender but cannot reroute it.
            sendCheckedResponse(preparedSender, frame);
        } catch (RuntimeException sendFailure) {
            this._receiver.removeHandlers(Connector2EpsMessageType.RequestResponse.toString(),
                    responseKey);
            originalFuture.completeExceptionally(sendFailure);
        }
        // The checked sender surfaces local transport errors. The observer
        // supplies only a frame key, never a delivery or Comm-ingress attestation.
    }
    public void sendMessageWithResponseString(Connector2EpsMessageType messageType, String pathString, JObject data, final DMessageResponseHandler responseHandler) {
        sendResponseFrame(messageType, pathString, data, null, responseHandler);
    }
    public void sendMessageWithResponse(Connector2EpsMessageType messageType, IPath path, JObject data, DMessageResponseHandler responseHandler) {
        String strPath = "";
        if (path != null) {
            strPath = path.toString();
        }
        this.sendMessageWithResponseString(messageType, strPath, data, responseHandler);
    }
    public void sendMessageWithResponse(Connector2EpsMessageType messageType, IPath path, JObject data,
            SetDataDiagnosticOrigin origin, DMessageResponseHandler responseHandler) {
        if (origin == null) {
            sendMessageWithResponse(messageType, path, data, responseHandler);
            return;
        }
        if (messageType != Connector2EpsMessageType.SetObjectData) {
            throw new IllegalArgumentException("Diagnostic origin applies only to SetObjectData");
        }
        String strPath = path != null ? path.toString() : "";
        sendResponseFrame(messageType, strPath, data, origin, responseHandler);
    }
    private void sendResponseFrame(Connector2EpsMessageType messageType, String pathString,
            JObject data, SetDataDiagnosticOrigin origin,
            DMessageResponseHandler responseHandler) {
        Connector2EpsMessage frame;
        IMessageSender boundSender;
        String responseKey;
        synchronized (diagnosticTransportLock) {
            if (retiredTransport) throw new IllegalStateException("Transport disconnected");
            int requestId = getRequestId();
            frame = _connector2EpsProtocol.encodeMessageWithResponse(messageType,
                    pathString, data, requestId,
                    authJoinMarkerV1Enabled ? origin : null);
            responseKey = Connector2EpsMessageType.RequestResponse + "_" + requestId;
            _receiver.addResponseHandler(Connector2EpsMessageType.RequestResponse,
                    String.valueOf(requestId), (json, error, context, requestedId) ->
                            responseHandler.accept(json, error, context));
            boundSender = _sender;
        }
        try {
            // A rebind cannot reroute this request onto its replacement sender.
            sendCheckedResponse(boundSender, frame);
        } catch (RuntimeException sendFailure) {
            _receiver.removeHandlers(Connector2EpsMessageType.RequestResponse.toString(),
                    responseKey);
            throw sendFailure;
        }
    }
    private static void sendCheckedResponse(IMessageSender sender, Connector2EpsMessage frame) {
        if (sender instanceof MessageSender) {
            ((MessageSender) sender).sendMessageChecked(frame);
        } else {
            sender.sendMessage(frame);
        }
    }
    int getRequestId() {
        int id;
        synchronized (this.requestIdLock) {
            (this._requestId)++;
            id = this._requestId;
        }
        return id;
    }
    public void sendInitMessage(String space, String type, JObject credentials) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeInitConnector(space, type, credentials));
    }
    public void sendRequestAnswer(Connector2EpsMessageType messageType, IPath path, JObject data, String targetEndpoint, int requestId) {
        this.sendRequestAnswerString(messageType, path.toString(), data, targetEndpoint, requestId);
    }
    public void sendRequestAnswerString(Connector2EpsMessageType messageType, String path, JObject data, String targetEndpoint, int requestId) {
        this._sender.sendMessage(this._connector2EpsProtocol.encodeMessageWithRequestId(messageType, path, data, targetEndpoint, requestId));
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
    public void addHandler(boolean isDelete, final Connector2EpsMessageType messageType, final IPath path, DCloudMessageHandler handler, final CompleteCallback completeCallback, HandlerRegistrationOptionsData options, final BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler) {
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
        int messageRequestId;
        if (isDelete) {
            this._receiver.removeHandlers((messageType.toString() + "_ListenTerminate"), path.toString());
            if (this._receiver.removeHandlers(messageType.toString(), path.toString())) {
                messageRequestId = this.getRequestId();
                this._sender.sendMessage(this._connector2EpsProtocol.encodeRemoveHandlerMessage(path.toString(), messageType, messageRequestId, ""));
            }
            else {
                completeCallback.accept(null);
                ;
                return ;
            }
        }
        else {
            messageRequestId = this.getRequestId();
        }
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
                    this._receiver.removeHandlers(messageType.toString(), path.toString());
                    APathContext ctx = new APathContext();
                    ctx.setPath(context.getPath());
                    ListenTerminateReason reason = (ListenTerminateReason) ListenTerminateReason.valueOf(json.getString("reason"));
                    listenTerminationHandler.accept(reason, ctx);
                    ;
                });
            }
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
        IMessageSender sender;
        synchronized (diagnosticTransportLock) {
            retireTransport();
            sender = this._sender;
            this._sender = CLOSED_SENDER;
        }
        this._receiver.destroy();
        // Closing a sender can synchronously deliver a network close callback.
        // It must not run while the manager lock is held.
        if (sender != null) sender.destroy();
    }
    public void addHandler(boolean isDelete, final Connector2EpsMessageType messageType, final IPath path, DCloudMessageHandler handler, final CompleteCallback completeCallback, HandlerRegistrationOptionsData options) {
        this.addHandler(isDelete, messageType, path, handler, completeCallback, options, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
}
