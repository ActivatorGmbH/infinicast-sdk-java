package io.infinicast.client.api;

import io.infinicast.Action;
import io.infinicast.JObject;
import io.infinicast.LogLevel;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IEndpointContext;
import io.infinicast.client.api.paths.options.CompleteCallback;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/**
 * the main object to communicate with the infinicat cloud.
 * it is mainly used to obtain a path or endpoint reference, as well as connection management
*/
public interface IInfinicastClient {
    /**
     * starst the fluent api call on a given infinicast path.
     * Nearly all infinicast functions are path based, so this is the main entrypoint.
     * @param path
     * @return
    */
    IPath path(String path);
    /**
     * Connects to Infinicast cloud to a given {@code space} via the specified {@code connectRole} and the provided {@code credentials}
     * @param address Adress of Infinicast Cloud. This specifies if you want to use the staging or live cloud. E.g. service.aplaypowered.com:7771
     * @param space Your Space name. A space is similar to a database name in usual databases.
     * @param connectRole The connection Role this client should be connected to. Article:ConnectRole
     * @param credentials Json credentials that can be passed to the authorisation service you defined
     * @param onResult Callback that gets triggered when the connection has been successfull or failed. ICError is null if the connection is sucessful.
    */
    void connectWithCredentials(String address, String space, String connectRole, JObject credentials, Consumer<ICError> onResult);
    /**
     * Connects to Infinicast cloud to a given {@code space} via the specified {@code conntectRole} and the provided {@code credentials}
     * @param address Adress of Infinicast Cloud. This specifies if you want to use the staging or live cloud. E.g. service.aplaypowered.com:7771
     * @param space Your Space name. A space is similar to a database name in usual databases.
     * @param conntectRole The connection Role this client should be connected to. Article:ConnectRole
     * @param credentials Json credentials that can be passed to the authorisation service you defined
     * @return Promise that will complete as soon as the connection has been established or throw an  if not.
    */
    CompletableFuture<Void> connectWithCredentialsAsync(String address, String space, String conntectRole, JObject credentials);
    /**
     * Connects to Infinicast cloud to a given {@code space} via the specified {@code conntectRole}
     * @param address Adress of Infinicast Cloud. This specifies if you want to use the staging or live cloud. E.g. service.aplaypowered.com:7771
     * @param space Your Space name. A space is similar to a database name in usual databases.
     * @param conntectRole The connection Role this client should be connected to. Article:ConnectRole
     * @param onResult Callback that gets triggered when the connection has been successfull or failed. ICError is null if the connection is sucessful.
    */
    void connect(String address, String space, String conntectRole, Consumer<ICError> onResult);
    /**
     * Connects to Infinicast cloud to a given {@code space} via the specified {@code conntectRole}
     * @param address Adress of Infinicast Cloud. This specifies if you want to use the staging or live cloud. E.g. service.aplaypowered.com:7771
     * @param space Your Space name. A space is similar to a database name in usual databases.
     * @param conntectRole The connection Role this client should be connected to. Article:ConnectRole
     * @return Promise that will complete as soon as the connection has been established or throw an  if not.
    */
    CompletableFuture<Void> connectAsync(String address, String space, String conntectRole);
    /**
     * Disconnects the client from the cloud.
    */
    void disconnect();
    /**
     * Disconnects the client from the cloud.
     * @return a promise that completes after the disconnect has been successfull
    */
    CompletableFuture<Void> disconnectAsync();
    /**
     * Registers a {@code handler} to be informed when the Client has been disconnected.
     * @param handler Handler to be informed when the Client has been disconnected.
    */
    void onDisconnect(Action handler);
    /**
     * Registers a {@code handler} to be informed when the Client has been disconnected.
     * @param handler Handler to be informed when the Client has been disconnected.
     * @return a promise that completes after the handler has been registered
    */
    CompletableFuture<Void> onDisconnectAsync(Action handler);
    /**
     * registers a listener that will be called when infinicast catches errors that should have been caught by the app.
     * @param errorHandler
    */
    void onUnhandeledError(BiConsumer<IPath, String> errorHandler);
    /**
     * get a reference to your own @see Infinicast.Client.Api.IEndpoint
     * @return an  that represents this clients Endpoint(http://infinicast.io/docs/Endpoint)
    */
    IEndpoint getOwnEndpoint();
    /**
     * get a reference to an @see Infinicast.Client.Api.IEndpoint by its {@code endpointId}
     * @param endpointId The Id the Endpoint is represented by see http://infinicast.io/docs/EndpointId
     * @return an  that represents the Endpoint(http://infinicast.io/docs/Endpoint)
    */
    IEndpoint getEndpointById(String endpointId);
    /**
     * get a reference to an @see Infinicast.Client.Api.IEndpoint by its {@code endpointPath}
     * @param endpointPath The PathImpl the Endpoint is represented by see http://infinicast.io/docs/EndpointPath
     * @return an  that represents the Endpoint(http://infinicast.io/docs/Endpoint)
    */
    IEndpoint getEndpointByPath(IPath endpointPath);
    /**
     * get a reference to an @see Infinicast.Client.Api.IEndpoint by its {@code endpointPath}
     * @param endpointPath The PathImpl the Endpoint is represented by see http://infinicast.io/docs/EndpointPath
     * @return an  that represents the Endpoint(http://infinicast.io/docs/Endpoint)
    */
    IEndpoint getEndpointByPathString(String endpointPath);
    /**
     * Returns Infinicast PathImpl for Endpoints see http://infinicast.io/docs/EndpointPath
     * @return The Infinicast PathImpl for Endpoints see http://infinicast.io/docs/EndpointPath
    */
    IPath getEndpointListPath();
    /**
     * registers a listener that will be triggered as soon as an endpoint of the givven {@code role} is disconnected
     * @param role
     * @param callback
     * @param registrationCompleteCallback
    */
    void onOtherEndpointDisconnected(String role, Consumer<IEndpointContext> callback, CompleteCallback registrationCompleteCallback);
    /**
     * registers a listener that will be triggered as soon as an endpoint of the givven {@code role} is disconnected
     * @param role
     * @param callback
     * @return
    */
    CompletableFuture<Void> onOtherEndpointDisconnectedAsync(String role, Consumer<IEndpointContext> callback);
    /**
     * allows to set the {@code logLevel} of internal infinicast log functions
     * @param logLevel
    */
    void setLogLevel(LogLevel logLevel);
    void systemCommand(String path, JObject data, Consumer<JObject> result);
    void introduceObjectToEndpoint(String address, IPath objekt);
    CompletableFuture<Void> introduceObjectToEndpointAsync(String address, IPath objekt);
    void updateDebugStatistics(JObject filters, Consumer<JObject> handler);
    CompletableFuture<JObject> updateDebugStatisticsAsync(JObject filters);
    /**
     * registers a listener that will be triggered as soon as an endpoint of the givven {@code role} is disconnected
     * @param role
     * @param callback
    */
    void onOtherEndpointDisconnected(String role, Consumer<IEndpointContext> callback);
}
