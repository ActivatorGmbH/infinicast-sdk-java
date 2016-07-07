package io.activator.infinicast.client.impl;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
public class StormSettings implements IStormSettings {
    ConnectorMessageManager _messageManager;
    public StormSettings(ConnectorMessageManager messageManager) {
        this._messageManager = messageManager;
    }
    public void createOrUpdateRole(String name, RoleSettings data) {
        this.createOrUpdateRole(name, data, (CompleteCallback) null);
    }
    public CompletableFuture<Void> createOrUpdateRoleAsync(String name, RoleSettings roleSettings) {
        CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.createOrUpdateRole(name, roleSettings, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    public void createOrUpdateRole(String name, RoleSettings data, CompleteCallback completeCallback) {
        JObject message = new JObject();
        message.set("data", data.toJson());
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.CreateOrUpdateRole, null, message, (json, context) -> {
            if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._messageManager.getConnector(), json, completeCallback, context.getPath()))) {
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
        CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.destroyRole(name, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    public void destroyRole(String name, CompleteCallback completeCallback) {
        JObject message = new JObject();
        message.set("name", name);
        this._messageManager.sendMessageWithResponse(Connector2EpsMessageType.DestroyRole, null, message, (json, context) -> {
            if (!(ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this._messageManager.getConnector(), json, completeCallback, context.getPath()))) {
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
