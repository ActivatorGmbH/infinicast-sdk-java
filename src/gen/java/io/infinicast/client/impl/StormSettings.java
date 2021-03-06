package io.infinicast.client.impl;

import io.infinicast.JObject;
import io.infinicast.client.api.IStormSettings;
import io.infinicast.client.api.RoleSettings;
import io.infinicast.client.api.errors.ICException;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.impl.helper.ErrorHandlingHelper;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.protocol.Connector2EpsMessageType;

import java.util.concurrent.CompletableFuture;
public class StormSettings implements IStormSettings {
    ConnectorMessageManager _messageManager;
    public StormSettings(ConnectorMessageManager messageManager) {
        this._messageManager = messageManager;
    }
    public void createOrUpdateRole(String name, RoleSettings data) {
        this.createOrUpdateRole(name, data, (CompleteCallback) null);
    }
    public CompletableFuture<Void> createOrUpdateRoleAsync(String name, RoleSettings roleSettings) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.createOrUpdateRole(name, roleSettings, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    public void createOrUpdateRole(String name, RoleSettings data, final CompleteCallback completeCallback) {
        JObject message = new JObject();
        message.set("data", data.toJson());
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.CreateOrUpdateRole, null, message, (json, error, context) -> {
            if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._messageManager.getConnector(), error, completeCallback, context.getPath()))) {
                if (completeCallback != null) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        });
    }
    public CompletableFuture<Void> destroyRoleAsync(String name) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.destroyRole(name, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    public void destroyRole(String name, final CompleteCallback completeCallback) {
        JObject message = new JObject();
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.DestroyRole, null, message, (json, error, context) -> {
            if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._messageManager.getConnector(), error, completeCallback, context.getPath()))) {
                if (completeCallback != null) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        });
    }
    public void destroyRole(String name) {
        this.destroyRole(name, (CompleteCallback) null);
    }
}
