package io.infinicast.client.impl;
import io.infinicast.*;



import io.infinicast.client.api.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.messaging.handlers.*;

public class StormSettings implements IStormSettings {
    ConnectorMessageManager _messageManager;
    public StormSettings(ConnectorMessageManager messageManager) {
        this._messageManager = messageManager;
    }
    public void createOrUpdateRole(String name, RoleSettings data) {
        this.createOrUpdateRole(name, data, (CompleteCallback) null);
    }
    public void createOrUpdateRole(String name, RoleSettings data, final CompleteCallback completeCallback) {
        StormSettings self = this;
        JObject message = new JObject();
        message.set("data", data.toJson());
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.CreateOrUpdateRole, null, message, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(_messageManager.getConnector(), json, completeCallback, context.getPath()))) {
                    if ((completeCallback != null)) {
                        completeCallback.accept(null);
                        ;
                    }
                }
                ;
            }
        }
        );
    }
    public void destroyRole(String name, final CompleteCallback completeCallback) {
        StormSettings self = this;
        JObject message = new JObject();
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.DestroyRole, null, message, new DMessageResponseHandler() {
            public void accept(JObject json, IPathAndEndpointContext context) {
                if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(_messageManager.getConnector(), json, completeCallback, context.getPath()))) {
                    if ((completeCallback != null)) {
                        completeCallback.accept(null);
                        ;
                    }
                }
                ;
            }
        }
        );
    }
    public void destroyRole(String name) {
        this.destroyRole(name, (CompleteCallback) null);
    }
}
