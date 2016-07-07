package io.infinicast.client.api;
import io.infinicast.JObject;
import io.infinicast.client.api.paths.EndpointConnectionInfo;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;

/**
 * An endpoint is a connected client in the infinicast cloud.
 * via this interface services can modify roles of the endpoints, disconnect them or simply get the id of an endpoint.
*/
public interface IEndpoint extends IPath {
    /**
     * removes a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param path
     * @param role
     * @param onComplete
    */
    void removeRole(IPath path, String role, CompleteCallback onComplete);
    /**
     * adds a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param pathString
     * @param role
    */
    void addRoleToStringPath(String pathString, String role);
    /**
     * adds a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param path
     * @param role
    */
    CompletableFuture<Void> addRoleAsync(IPath path, String role);
    /**
     * adds a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param pathString
     * @param role
    */
    CompletableFuture<Void> addRoleToStringPathAsync(String pathString, String role);
    /**
     * adds a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param pathString
     * @param role
     * @param onComplete
    */
    void addRoleToStringPath(String pathString, String role, CompleteCallback onComplete);
    /**
     * adds a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter.
     * note: path wildcards are valid paths for roles
     * @param path
     * @param role
    */
    void addRole(IPath path, String role);
    /**
     * removes a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param path
     * @param role
    */
    CompletableFuture<Void> removeRoleAsync(IPath path, String role);
    /**
     * removes a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param pathString
     * @param role
    */
    CompletableFuture<Void> removeRoleFromStringPathAsync(String pathString, String role);
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code path}
     * @param path
     * @param roleListHandler
    */
    void getRoles(IPath path, DRoleListHandler roleListHandler);
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code pathString}
     * @param pathString
     * @param roleListHandler
    */
    void getRolesForStringPath(String pathString, DRoleListHandler roleListHandler);
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code path}
     * @param path
    */
    CompletableFuture<ArrayList<String>> getRolesAsync(IPath path);
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code pathString}
     * @param pathString
    */
    CompletableFuture<ArrayList<String>> getRolesForStringPathAsync(String pathString);
    /**
     * returns the endpointconnectinfo of the given endpoint.
     * The IPAdress is an example of the information available.
     * @param result
    */
    void getEndpointConnectionInfo(BiConsumer<ErrorInfo, EndpointConnectionInfo> result);
    /**
     * returns the endpointconnectinfo of the given endpoint.
     * The IPAdress is an example of the information available.
    */
    CompletableFuture<EndpointConnectionInfo> getEndpointConnectionInfoAsync();
    void setDebugName(String name);
    /**
     * adds a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter.
     * note: path wildcards are valid paths for roles
     * @param path
     * @param role
     * @param onComplete
    */
    void addRole(IPath path, String role, CompleteCallback onComplete);
    /**
     * endpoint Id of this endpoint
    */
    String getEndpointId();
    /**
     * removes a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param path
     * @param role
    */
    void removeRole(IPath path, String role);
    /**
     * removes a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param pathString
     * @param role
     * @param onComplete
    */
    void removeRoleFromStringPath(String pathString, String role, CompleteCallback onComplete);
    /**
     * removes a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param pathString
     * @param role
    */
    void removeRoleFromStringPath(String pathString, String role);
    void introduce(IPath objekt, JObject infoJson);
    void introduce(IPath objekt);
}
