package io.infinicast.client.impl;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    public void createOrUpdateRole(String name, RoleSettings data, final CompleteCallback completeCallback) {
        JObject message = new JObject();
        message.set("data", data.toJson());
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.CreateOrUpdateRole, null, message, (json, error, context) -> {
            if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._messageManager.getConnector(), error, completeCallback, context.getPath()))) {
                if ((completeCallback != null)) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        }
        );
    }
    public CompletableFuture<Void> destroyRoleAsync(String name) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.destroyRole(name, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    public void destroyRole(String name, final CompleteCallback completeCallback) {
        JObject message = new JObject();
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.DestroyRole, null, message, (json, error, context) -> {
            if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._messageManager.getConnector(), error, completeCallback, context.getPath()))) {
                if ((completeCallback != null)) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        }
        );
    }
    public void destroyRole(String name) {
        this.destroyRole(name, (CompleteCallback) null);
    }
}
