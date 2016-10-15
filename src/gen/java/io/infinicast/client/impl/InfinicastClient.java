package io.infinicast.client.impl;
import io.infinicast.*;



import io.infinicast.client.api.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;

/**
 * Everything in Infinicast is using paths. Paths are the way to share anything:
 * paths can be used to store data, send requests and send messages.
 * all data, requests, messages can be listened on and live updates can be received.
*/
public class InfinicastClient extends PathImpl  implements IPath, IInfinicastClient, IConnector {
    IEndpoint2ServerNetLayer _endpoint2ServerNetLayer;
    ObjectStateManager _objectStateManager;
    String _role = "";
    String _space = "";
    Endpoint _thisEndpoint;
    JObject _credentials = null;
    Logger _ClientLogger = LoggerFactory.getLogger(InfinicastClient.class);
    Consumer<ErrorInfo> _onConnect;
    Action _onDisconnect;
    BiConsumer<IPath, String> _unhandeledErrorHandler;
    public InfinicastClient() {
        super("");
        this._ClientLogger.info(("Infinicast Client " + VersionHelper.getClientVersion()));
    }
    void setCredentials(JObject credentials) {
        this._credentials = credentials;
    }
    public void connectWithCredentials(String address, String space, String conntectRole, JObject credentials, Consumer<ErrorInfo> onConnect_) {
        this.init();
        try {
            this._onConnect = onConnect_;
            InfinicastClient connector = this;
            TcpEndpoint2ServerNetLayer e2SNetLayer = new TcpEndpoint2ServerNetLayer();
            Endpoint2ServerNetSettings netSettings = new Endpoint2ServerNetSettings();
            netSettings.setServerAddress(NetFactory.createServerAddress(address));
            connector.setSpace(space);
            connector.setRole(conntectRole);
            connector.setCredentials(credentials);
            netSettings.setHandler(super.messageManager);
            this._endpoint2ServerNetLayer = e2SNetLayer;
            super.messageManager.setSender(new MessageSender(e2SNetLayer));
            super.messageManager.setConnector(this);
            String result = e2SNetLayer.Open(netSettings);
            if (!(StringExtensions.IsNullOrEmpty(result))) {
                onConnect_.accept(ErrorInfo.fromMessage(result, ""));
                ;
            }
        }
        catch (Exception e) {
            this._onConnect.accept(ErrorInfo.fromMessage(StringExtensions.formatException(e), ""));
            ;
        }
    }

    public void connect(String address, String space, String conntectRole, Consumer<ErrorInfo> onConnect_) {
        this.connectWithCredentials(address, space, conntectRole, null, onConnect_);
    }

    /**
     * Registers a {@code handler} to be informed when the Client has been disconnected.
     * @param handler Handler to be informed when the Client has been disconnected.
    */
    public void onDisconnect(Action handler) {
        this._onDisconnect = handler;
    }

    /**
     * Disconnects the client from the cloud.
    */
    public void disconnect() {
        if ((this._endpoint2ServerNetLayer != null)) {
            this._endpoint2ServerNetLayer.Close();
        }
    }

    /**
     * get a reference to your own @see Infinicast.Client.Api.IEndpoint
     * @return an  that represents this clients Endpoint(http://infinicast.io/docs/Endpoint)
    */
    public IEndpoint getOwnEndpoint() {
        return this._thisEndpoint;
    }
    /**
     * get a reference to an @see Infinicast.Client.Api.IEndpoint by its {@code endpointId}
     * @param endpointId The Id the Endpoint is represented by see http://infinicast.io/docs/EndpointId
     * @return an  that represents the Endpoint(http://infinicast.io/docs/Endpoint)
    */
    public IEndpoint getEndpointById(String endpointId) {
        String path = PathUtils.getEndpointPath(endpointId);
        Endpoint endpointObject = new Endpoint(path, endpointId, this);
        return endpointObject;
    }
    /**
     * get a reference to an @see Infinicast.Client.Api.IEndpoint by its {@code endpointPath}
     * @param endpointPath The PathImpl the Endpoint is represented by see http://infinicast.io/docs/EndpointPath
     * @return an  that represents the Endpoint(http://infinicast.io/docs/Endpoint)
    */
    public IEndpoint getEndpointByPath(IPath endpointPath) {
        String path = endpointPath.toString();
        if (!(path.startsWith("/~endpoints/"))) {
            throw new RuntimeException(new Exception("not a valid Endpoint path!"));
        }
        String endpointId = path.substring(12);
        endpointId = StringExtensions.removeFrom(endpointId, endpointId.lastIndexOf("/"));
        Endpoint endpointObject = new Endpoint(path, endpointId, this);
        return endpointObject;
    }
    /**
     * get a reference to an @see Infinicast.Client.Api.IEndpoint by its {@code endpointPath}
     * @param endpointPath The PathImpl the Endpoint is represented by see http://infinicast.io/docs/EndpointPath
     * @return an  that represents the Endpoint(http://infinicast.io/docs/Endpoint)
    */
    public IEndpoint getEndpointByPathString(String endpointPath) {
        String path = endpointPath;
        if (!(endpointPath.startsWith("/~endpoints/"))) {
            throw new RuntimeException(new Exception("not a valid Endpoint path!"));
        }
        String endpointId = endpointPath.substring(12);
        endpointId = StringExtensions.removeFrom(endpointId, endpointId.lastIndexOf("/"));
        Endpoint endpointObject = new Endpoint(path, endpointId, this);
        return endpointObject;
    }
    /**
     * Returns Infinicast PathImpl for Endpoints see http://infinicast.io/docs/EndpointPath
     * @return The Infinicast PathImpl for Endpoints see http://infinicast.io/docs/EndpointPath
    */
    public IPath getEndpointListPath() {
        return super.path("/~endpoints/");
    }
    public void systemCommand(String path, JObject data, final Consumer<JObject> result) {
        InfinicastClient self = this;
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.SystemCommand, path, data, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                result.accept(json);
                ;
            }
        }
        );
    }
    public void systemCommandWithHandler(String path, JObject data, final Consumer<JObject> onEvent, final CompleteCallback registrationCompleteHandler) {
        InfinicastClient self = this;
        if (data.containsNonNull("type")) {
            String type = data.getString("type");
            if ((!(StringExtensions.IsNullOrEmpty(type)) && StringExtensions.areEqual(type, "registerMsgDebugger"))) {
                if ((onEvent == null)) {
                    data.set("remove", true);
                    super.messageManager.registerHandler(Connector2EpsMessageType.DebugObserverMessage, super.path(PathUtils.infinicastInternStart), null);
                }
                else {
                    super.messageManager.registerHandler(Connector2EpsMessageType.DebugObserverMessage, super.path(PathUtils.infinicastInternStart), new DCloudMessageHandler() {
                        public void accept(JObject json, IPathAndEndpointContext context, int id) {
                            onEvent.accept(json);
                            ;
                        }
                    }
                    );
                }
                super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.SystemCommand, path, data, new DMessageResponseHandler() {
                    public void accept(JObject json, IPathAndEndpointContext context) {
                        if (((json == null) || json.containsNonNull("error"))) {
                            registrationCompleteHandler.accept(ErrorInfo.fromMessage(json.getString("error"), ""));
                            ;
                        }
                        else {
                            registrationCompleteHandler.accept(null);
                            ;
                        }
                        ;
                    }
                }
                );
            }
        }
    }
    public void pathRoleSetup(String path, String role, PathRoleSettings pathSettings) {
        this.pathRoleSetup(path, role, pathSettings, (CompleteCallback) null);
    }
    public void introduceObjectToEndpoint(String address, IPath objekt) {
        JObject data = new JObject();
        data.set("target", address);
        super.messageManager.sendMessage(Connector2EpsMessageType.IntroduceObject, objekt, data);
    }
    public void updateDebugStatistics(JObject filters, Consumer<JObject> handler) {
        super.messageManager.sendUpdateDebugStatistics(filters, handler);
    }
    /**
     * allows to set the {@code logLevel} of internal infinicast log functions
     * @param logLevel
    */
    public void setLogLevel(LogLevel logLevel) {
        LoggerSettings.CurrentLogLevel = logLevel;
    }
    public String getRole() {
        return this._role;
    }
    public String getSpace() {
        return this._space;
    }
    public void triggerDisconnect() {
        this._ClientLogger.info("disconnected");
        Console.WriteLine("Disconnect triggered");
        if ((this._onDisconnect != null)) {
            this._onDisconnect.accept();
            ;
        }
    }
    public void unhandeledError(IPath iaPath, JObject errorJson) {
        String text = "";
        if ((errorJson != null)) {
            text = errorJson.toString();
        }
        if ((this._unhandeledErrorHandler != null)) {
            this._unhandeledErrorHandler.accept(iaPath, text);
            ;
            return ;
        }
        if ((iaPath != null)) {
            text = (" path: " + iaPath.toString());
        }
        this._ClientLogger.error(("an Unhandeled Error occured: " + text));
    }
    public void unhandeledErrorInfo(IPath iaPath, ErrorInfo errorJson) {
        if ((this._unhandeledErrorHandler != null)) {
            this._unhandeledErrorHandler.accept(iaPath, errorJson.getMessage());
            ;
            return ;
        }
        String text = errorJson.getMessage();
        if ((iaPath != null)) {
            text = (" path: " + iaPath.toString());
        }
        this._ClientLogger.error(("an Unhandeled Error occured: " + text));
    }
    public JObject getCredentials() {
        return this._credentials;
    }
    public void onInitConnector(JObject data, JObject endPoint) {
        if (((data != null) && (data.get("error") != null))) {
            this._onConnect.accept(ErrorInfo.fromJson(data.getJObject("error"), ""));
            ;
        }
        else {
            this._thisEndpoint = new Endpoint(endPoint.getString("path"), endPoint.getString("endpoint"), this);
            this._onConnect.accept(null);
            ;
        }
    }
    public ObjectStateManager getObjectStateManager() {
        return this._objectStateManager;
    }
    public PathImpl getRootPath() {
        return this;
    }
    void init() {
        if ((this._objectStateManager == null)) {
            super.messageManager = new ConnectorMessageManager();
            this._objectStateManager = new ObjectStateManager(this);
            super.setConnector(this);
        }
    }
    /**
     * registers a listener that will be called when infinicast catches errors that should have been caught by the app.
     * @param errorHandler
    */
    public void onUnhandeledError(BiConsumer<IPath, String> errorHandler) {
        this._unhandeledErrorHandler = errorHandler;
    }
    public void pathRoleSetup(String path, String role, PathRoleSettings pathSettings, final CompleteCallback onComplete) {
        InfinicastClient self = this;
        if (!(StringExtensions.IsNullOrEmpty(path))) {
            path = PathUtils.cleanup(path);
        }
        JObject message = new JObject();
        if ((pathSettings != null)) {
            message.set("data", pathSettings.toJson());
        }
        message.set("role", role);
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.PathRoleSetup, path, message, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersFull(getConnector(), json, onComplete, context.getPath());
            }
        }
        );
    }
    public void setRole(String role) {
        this._role = role;
    }
    public void setSpace(String space) {
        this._space = space;
    }
    public IEndpoint2ServerNetLayerHandler getEndpointHandler() {
        return super.messageManager;
    }
    public IStormSettings settings() {
        return new StormSettings(super.messageManager);
    }
    public IPath getEndpointPath(String address) {
        return super.path((("/~endpoints/" + address) + "/"));
    }
    /**
     * registers a listener that will be triggered as soon as an endpoint of the givven {@code role} is disconnected
     * @param role
     * @param callback
     * @param registrationCompleteCallback
    */
    public void onOtherEndpointDisconnected(String role, final Consumer<IEndpointContext> callback, CompleteCallback registrationCompleteCallback) {
        InfinicastClient self = this;
        super.messageManager.addHandler(false, Connector2EpsMessageType.EndpointDisconnected, super.path(PathUtils.endpointDisconnectedByRolePath(role)), new DCloudMessageHandler() {
            public void accept(JObject json, IPathAndEndpointContext context, int id) {
                APEndpointContext endpointContext = new APEndpointContext();
                endpointContext.setEndpoint(context.getEndpoint());
                endpointContext.setEndpointData(context.getEndpointData());
                callback.accept(endpointContext);
                ;
            }
        }
        , registrationCompleteCallback, null);
    }
    /**
     * registers a listener that will be triggered as soon as an endpoint of the givven {@code role} is disconnected
     * @param role
     * @param callback
    */
    public void onOtherEndpointDisconnected(String role, final Consumer<IEndpointContext> callback) {
        this.onOtherEndpointDisconnected(role, callback, (CompleteCallback) null);
    }
}
