package io.infinicast.client.impl.query;
import io.infinicast.JObject;
import io.infinicast.Logger;
import io.infinicast.LoggerFactory;
import io.infinicast.TriConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.HandlerRegistrationOptions;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.api.paths.handler.messages.APMessageCallback;
import io.infinicast.client.api.paths.handler.messages.APValidateDataChangeCallback;
import io.infinicast.client.api.paths.handler.messages.APValidateMessageCallback;
import io.infinicast.client.api.paths.handler.objects.APObjectIntroduceCallback;
import io.infinicast.client.api.paths.handler.reminders.AReminderCallback;
import io.infinicast.client.api.paths.handler.requests.APRequestCallback;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.query.ListenTerminateReason;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.pathAccess.RequestResponder;
import io.infinicast.client.impl.responder.ValidationResponder;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.activator.infinicast.*;

import java.util.function.*;

public class PathQueryWithHandlerExecutor extends BaseQueryExecutor  {
    Logger _logger = LoggerFactory.getLogger(PathQueryWithHandlerExecutor.class);
    public PathQueryWithHandlerExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler(false, Connector2EpsMessageType.SetObjectData, super._path, (json, context, id) -> {
            JObject newOb = null;
            JObject oldOb = null;
            if (json.containsNonNull("new")) {
                newOb = json.getJObject("new");
            }
            if (json.containsNonNull("old")) {
                oldOb = json.getJObject("old");
            }
            callback.accept(newOb, oldOb, context);
            ;
        }
        , completeCallback, options);
    }
    public void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback, HandlerRegistrationOptions options) {
        this.onDataChange(callback, options, (CompleteCallback) null);
    }
    public void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback) {
        this.onDataChange(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onValidateDataChange(APValidateDataChangeCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.DataChangeValidate, super._path, (json, context, id) -> {
            JObject newOb = null;
            JObject oldOb = null;
            if (json.containsNonNull("new")) {
                newOb = json.getJObject("new");
            }
            if (json.containsNonNull("old")) {
                oldOb = json.getJObject("old");
            }
            callback.accept(newOb, oldOb, new ValidationResponder(Connector2EpsMessageType.DataChangeValidated, super._messageManager, newOb, context.getPath(), context.getEndpoint()), context);
            ;
        }
        , completeCallback, options);
    }
    public void onValidateDataChange(APValidateDataChangeCallback callback, HandlerRegistrationOptions options) {
        this.onValidateDataChange(callback, options, (CompleteCallback) null);
    }
    public void onValidateDataChange(APValidateDataChangeCallback callback) {
        this.onValidateDataChange(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onValidateMessage(APValidateMessageCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.MessageValidate, super._path, (json, context, id) -> {
            callback.accept(json, new ValidationResponder(Connector2EpsMessageType.MessageValidated, super._messageManager, json, context.getPath(), context.getEndpoint()), context);
            ;
        }
        , completeCallback, options);
    }
    public void onValidateMessage(APValidateMessageCallback callback, HandlerRegistrationOptions options) {
        this.onValidateMessage(callback, options, (CompleteCallback) null);
    }
    public void onValidateMessage(APValidateMessageCallback callback) {
        this.onValidateMessage(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onMessage(APMessageCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback, BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler) {
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.Message, super._path, (json, context, id) -> {
            callback.accept(json, context);
            ;
        }
        , completeCallback, options, listenTerminationHandler);
    }
    public void onMessage(APMessageCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        this.onMessage(callback, options, completeCallback, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    public void onMessage(APMessageCallback callback, HandlerRegistrationOptions options) {
        this.onMessage(callback, options, (CompleteCallback) null, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    public void onMessage(APMessageCallback callback) {
        this.onMessage(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    public void onRequest(APRequestCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.Request, super._path, (json, context, requestId) -> {
            callback.accept(json, new RequestResponder(super._messageManager, context.getPath(), context.getEndpoint().getEndpointId(), requestId), context);
            ;
        }
        , completeCallback, options);
    }
    public void onRequest(APRequestCallback callback, HandlerRegistrationOptions options) {
        this.onRequest(callback, options, (CompleteCallback) null);
    }
    public void onRequest(APRequestCallback callback) {
        this.onRequest(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onReminder(AReminderCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.Reminder, super._path, (json, context, id) -> {
            callback.accept(json, context);
            ;
        }
        , completeCallback, options);
    }
    public void onReminder(AReminderCallback callback, HandlerRegistrationOptions options) {
        this.onReminder(callback, options, (CompleteCallback) null);
    }
    public void onReminder(AReminderCallback callback) {
        this.onReminder(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onIntroduce(APObjectIntroduceCallback callback, CompleteCallback completeCallback) {
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.IntroduceObject, super._path, (json, context, id) -> {
            callback.accept(json, context);
            ;
        }
        , completeCallback, null);
    }
    public void onIntroduce(APObjectIntroduceCallback callback) {
        this.onIntroduce(callback, (CompleteCallback) null);
    }
}
