package io.infinicast.client.impl.query;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
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
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.impl.pathAccess.RequestResponder;
import io.infinicast.client.impl.responder.ValidationResponder;
import io.infinicast.client.protocol.Connector2EpsMessageType;
public class PathQueryWithHandlerExecutor extends BaseQueryExecutor  {
    Logger _logger = LoggerFactory.getLogger(PathQueryWithHandlerExecutor.class);
    public PathQueryWithHandlerExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void onDataChange(final TriConsumer<JObject, JObject, IPathAndEndpointContext> callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler(false, Connector2EpsMessageType.SetObjectData, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
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
        }
        , completeCallback, options);
    }
    public void onDataChange(final TriConsumer<JObject, JObject, IPathAndEndpointContext> callback, HandlerRegistrationOptions options) {
        this.onDataChange(callback, options, (CompleteCallback) null);
    }
    public void onDataChange(final TriConsumer<JObject, JObject, IPathAndEndpointContext> callback) {
        this.onDataChange(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onValidateDataChange(final APValidateDataChangeCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.DataChangeValidate, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                JObject newOb = null;
                JObject oldOb = null;
                if (json.containsNonNull("new")) {
                    newOb = json.getJObject("new");
                }
                if (json.containsNonNull("old")) {
                    oldOb = json.getJObject("old");
                }
                callback.accept(newOb, oldOb, new ValidationResponder(Connector2EpsMessageType.DataChangeValidated, _messageManager, newOb, context.getPath(), context.getEndpoint()), context);
                ;
            }
        }
        , completeCallback, options);
    }
    public void onValidateDataChange(final APValidateDataChangeCallback callback, HandlerRegistrationOptions options) {
        this.onValidateDataChange(callback, options, (CompleteCallback) null);
    }
    public void onValidateDataChange(final APValidateDataChangeCallback callback) {
        this.onValidateDataChange(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onValidateMessage(final APValidateMessageCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.MessageValidate, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                callback.accept(json, new ValidationResponder(Connector2EpsMessageType.MessageValidated, _messageManager, json, context.getPath(), context.getEndpoint()), context);
                ;
            }
        }
        , completeCallback, options);
    }
    public void onValidateMessage(final APValidateMessageCallback callback, HandlerRegistrationOptions options) {
        this.onValidateMessage(callback, options, (CompleteCallback) null);
    }
    public void onValidateMessage(final APValidateMessageCallback callback) {
        this.onValidateMessage(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onMessage(final APMessageCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback, BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.Message, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                callback.accept(json, context);
                ;
            }
        }
        , completeCallback, options, listenTerminationHandler);
    }
    public void onMessage(final APMessageCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        this.onMessage(callback, options, completeCallback, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    public void onMessage(final APMessageCallback callback, HandlerRegistrationOptions options) {
        this.onMessage(callback, options, (CompleteCallback) null, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    public void onMessage(final APMessageCallback callback) {
        this.onMessage(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    public void onRequest(final APRequestCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.Request, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int requestId) {
                _logger.info((((((((("received request " + requestId) + " ") + _path.toString()) + " endpoint: ") + context.getEndpoint().getEndpointId()) + " data ") + context.getEndpointData()) + " path ") + context.getPath().toString());
                RequestResponder responder = new RequestResponder(_messageManager, context.getPath(), context.getEndpoint().getEndpointId(), requestId);
                callback.accept(json, responder, context);
                ;
                if (!(responder.alreadyResponded())) {
                    _connector.getRequestResponseManager().addResponder(responder);
                }
            }
        }
        , completeCallback, options);
    }
    public void onRequest(final APRequestCallback callback, HandlerRegistrationOptions options) {
        this.onRequest(callback, options, (CompleteCallback) null);
    }
    public void onRequest(final APRequestCallback callback) {
        this.onRequest(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onReminder(final AReminderCallback callback, HandlerRegistrationOptions options, CompleteCallback completeCallback) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.Reminder, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                callback.accept(json, context);
                ;
            }
        }
        , completeCallback, options);
    }
    public void onReminder(final AReminderCallback callback, HandlerRegistrationOptions options) {
        this.onReminder(callback, options, (CompleteCallback) null);
    }
    public void onReminder(final AReminderCallback callback) {
        this.onReminder(callback, (HandlerRegistrationOptions) null, (CompleteCallback) null);
    }
    public void onIntroduce(final APObjectIntroduceCallback callback, CompleteCallback completeCallback) {
        PathQueryWithHandlerExecutor self = this;
        super._messageManager.addHandler((callback == null), Connector2EpsMessageType.IntroduceObject, super._path, new DCloudMessageHandler() {
            public void accept(JObject json, ICError err, IPathAndEndpointContext context, int id) {
                callback.accept(json, context);
                ;
            }
        }
        , completeCallback, null);
    }
    public void onIntroduce(final APObjectIntroduceCallback callback) {
        this.onIntroduce(callback, (CompleteCallback) null);
    }
}
