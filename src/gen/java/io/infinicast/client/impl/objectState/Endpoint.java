package io.infinicast.client.impl.objectState;
import io.infinicast.*;

import java.util.*;


import io.infinicast.client.api.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.messaging.handlers.*;

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

    public void getRoles(IPath rolePath, DRoleListHandler roleListHandler) {
        this.getRolesForStringPath(rolePath.toString(), roleListHandler);
    }
    /**
     * returns a list of the roles the endpoint fullfills for the given {@code pathString}
     * @param pathString
     * @param roleListHandler
    */
    public void getRolesForStringPath(String pathString, final DRoleListHandler roleListHandler) {
        Endpoint self = this;
        JObject message = new JObject();
        message.set("target", this.getEndpointId());
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.GetRoleForPath, pathString, message, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(json, new CompleteCallback() {
                    public void accept(ErrorInfo error) {
                        roleListHandler.accept(error, null);
                        ;
                    }
                }
                ))) {
                    ArrayList<String> list = json.getStringArray("list");
                    roleListHandler.accept(null, list);
                    ;
                }
                ;
            }
        }
        );
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
        Endpoint self = this;
        JObject message = new JObject();
        message.set("target", this.getEndpointId());
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.GetEndpointConnectionInfo, "", message, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                onGetEndpointConnectionInfoResponse(result, json);
            }
        }
        );
    }
    void onGetEndpointConnectionInfoResponse(final BiConsumer<ErrorInfo, EndpointConnectionInfo> result, JObject json) {
        Endpoint self = this;
        if (!(super.checkIfHasErrorsAndCallHandlersNew(json, new CompleteCallback() {
            public void accept(ErrorInfo error) {
                result.accept(error, null);
                ;
            }
        }
        ))) {
            EndpointConnectionInfo info = new EndpointConnectionInfo();
            info.ipAddress = json.getString("ipAddress");
            result.accept(null, info);
            ;
        }
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
        Endpoint self = this;
        JObject data = new JObject();
        data.set("modifier", modifier);
        data.set("role", role);
        data.set("target", this.getEndpointId());
        super.messageManager.sendMessageWithResponseString(Connector2EpsMessageType.ModifyRoleForPath, pathString, data, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                if (!(checkIfHasErrorsAndCallHandlersNew(json, onComplete))) {
                    if ((onComplete != null)) {
                        onComplete.accept(null);
                        ;
                    }
                }
                ;
            }
        }
        );
    }
    void sendModifyRole(String modifier, String pathString, String role) {
        this.sendModifyRole(modifier, pathString, role, (CompleteCallback) null);
    }
}
