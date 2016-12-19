package io.infinicast.client.impl.objectState;

import io.infinicast.JObject;
import io.infinicast.client.api.DRoleListHandler;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.AfinityException;
import io.infinicast.client.api.paths.EndpointConnectionInfo;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.protocol.Connector2EpsMessageType;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
/**
 * Everything in Infinicast is using paths. Paths are the way to share anything:
 * paths can be used to store data, send requests and send messages.
 * all data, requests, messages can be listened on and live updates can be received.
*/
public class Endpoint extends PathImpl  implements IEndpoint {
    String _endpointId;
    public Endpoint(String path, String targetAddress, PathImpl root) {
        super(path);
        super.setRoot(root);
        this.setEndpointId(targetAddress);
    }
    /**
     * adds a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param pathString
     * @param role
    */
    public void addRoleToStringPath(String pathString, String role) {
        this.addRoleToStringPath(pathString, role, (CompleteCallback) null);
    }
    /**
     * adds a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param pathString
     * @param role
     * @param onComplete
    */
    public void addRoleToStringPath(String pathString, String role, CompleteCallback onComplete) {
        this.sendModifyRole("add", pathString, role, onComplete);
    }
    /**
     * adds a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter.
     * note: path wildcards are valid paths for roles
     * @param path
     * @param role
    */
    public void addRole(IPath path, String role) {
        this.addRole(path, role, (CompleteCallback) null);
    }
    /**
     * adds a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param path
     * @param role
    */
    public CompletableFuture<Void> addRoleAsync(IPath path, String role) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.addRole(path, role, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * adds a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * @param pathString
     * @param role
    */
    public CompletableFuture<Void> addRoleToStringPathAsync(String pathString, String role) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.addRoleToStringPath(pathString, role, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * adds a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter.
     * note: path wildcards are valid paths for roles
     * @param path
     * @param role
     * @param onComplete
    */
    public void addRole(IPath path, String role, CompleteCallback onComplete) {
        this.addRoleToStringPath(path.toString(), role, onComplete);
    }
    public void introduce(IPath objekt) {
        this.introduce(objekt, (JObject) null);
    }
    /**
     * removes a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param path
     * @param role
    */
    public CompletableFuture<Void> removeRoleAsync(IPath path, String role) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.removeRole(path, role, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * removes a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param pathString
     * @param role
    */
    public CompletableFuture<Void> removeRoleFromStringPathAsync(String pathString, String role) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.removeRoleFromStringPath(pathString, role, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    public void getRoles(IPath rolePath, DRoleListHandler roleListHandler) {
        this.getRolesForStringPath(rolePath.toString(), roleListHandler);
    }
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code pathString}
     * @param pathString
     * @param roleListHandler
    */
    public void getRolesForStringPath(String pathString, final DRoleListHandler roleListHandler) {
        JObject message = new JObject();
        message.set("target", this.getEndpointId());
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.GetRoleForPath, pathString, message, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                roleListHandler.accept(error, null);
                ;
            }))) {
                ArrayList<String> list = json.getStringArray("list");
                roleListHandler.accept(null, list);
                ;
            }
            ;
        });
    }
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code path}
     * @param path
    */
    public CompletableFuture<ArrayList<String>> getRolesAsync(IPath path) {
        return this.getRolesForStringPathAsync(path.toString());
    }
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code pathString}
     * @param pathString
    */
    public CompletableFuture<ArrayList<String>> getRolesForStringPathAsync(String pathString) {
        final CompletableFuture<ArrayList<String>> tcs = new CompletableFuture<ArrayList<String>>();
        this.getRolesForStringPath(pathString, (error, roles) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(roles);
            }
            ;
        });
        return tcs;
    }
    public void setDebugName(String name) {
        JObject data = new JObject();
        data.set("target", this.getEndpointId());
        data.set("name", name);
        super.messageManager.sendMessageString(Connector2EpsMessageType.SetDebugName, "", data);
    }
    /**
     * returns the endpointconnectinfo of the given endpoint.
     * The IPAdress is an example of the information available.
     * @param result
    */
    public void getEndpointConnectionInfo(final BiConsumer<ErrorInfo, EndpointConnectionInfo> result) {
        JObject message = new JObject();
        message.set("target", this.getEndpointId());
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.GetEndpointConnectionInfo, "", message, (json, context) -> {
            this.onGetEndpointConnectionInfoResponse(result, json);
        });
    }
    void onGetEndpointConnectionInfoResponse(final BiConsumer<ErrorInfo, EndpointConnectionInfo> result, JObject json) {
        if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
            result.accept(error, null);
            ;
        }))) {
            EndpointConnectionInfo info = new EndpointConnectionInfo();
            info.ipAddress = json.getString("ipAddress");
            result.accept(null, info);
            ;
        }
    }
    /**
     * returns the endpointconnectinfo of the given endpoint.
     * The IPAdress is an example of the information available.
    */
    public CompletableFuture<EndpointConnectionInfo> getEndpointConnectionInfoAsync() {
        final CompletableFuture<EndpointConnectionInfo> tcs = new CompletableFuture<EndpointConnectionInfo>();
        this.getEndpointConnectionInfo((error, info) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(info);
            }
            ;
        });
        return tcs;
    }
    public void introduce(IPath objekt, JObject infoJson) {
        JObject data = new JObject();
        data.set("target", this.getEndpointId());
        super.messageManager.sendMessage(Connector2EpsMessageType.IntroduceObject, objekt, data);
    }
    /**
     * endpoint Id of this endpoint
    */
    public String getEndpointId() {
        return this._endpointId;
    }
    public void setEndpointId(String value) {
        this._endpointId = value;
    }
    /**
     * removes a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param path
     * @param role
     * @param onComplete
    */
    public void removeRole(IPath path, String role, CompleteCallback onComplete) {
        this.removeRoleFromStringPath(path.toString(), role, onComplete);
    }
    /**
     * removes a role to the given {@code path}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param path
     * @param role
    */
    public void removeRole(IPath path, String role) {
        this.removeRole(path, role, (CompleteCallback) null);
    }
    /**
     * removes a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param pathString
     * @param role
     * @param onComplete
    */
    public void removeRoleFromStringPath(String pathString, String role, CompleteCallback onComplete) {
        this.sendModifyRole("remove", pathString, role, onComplete);
    }
    /**
     * removes a role to the given {@code pathString}.
     * multiple roles cann be passed by a comma seperated list in the {@code role} parameter
     * note: path wildcards are valid paths for roles
     * note: for removing roles wildcards can be used. for example RemoveRole(...,"*",...)
     * @param pathString
     * @param role
    */
    public void removeRoleFromStringPath(String pathString, String role) {
        this.removeRoleFromStringPath(pathString, role, (CompleteCallback) null);
    }
    void sendModifyRole(String modifier, String pathString, String role, final CompleteCallback onComplete) {
        JObject data = new JObject();
        data.set("modifier", modifier);
        data.set("role", role);
        data.set("target", this.getEndpointId());
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.ModifyRoleForPath, pathString, data, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, onComplete))) {
                if (onComplete != null) {
                    onComplete.accept(null);
                    ;
                }
            }
            ;
        });
    }
    void sendModifyRole(String modifier, String pathString, String role) {
        this.sendModifyRole(modifier, pathString, role, (CompleteCallback) null);
    }
}
