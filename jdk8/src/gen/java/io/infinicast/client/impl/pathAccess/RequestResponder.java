package io.infinicast.client.impl.pathAccess;
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
public class RequestResponder implements IAPResponder {
    ConnectorMessageManager _messageManager;
    IPath _path;
    int _requestId;
    String _targetEndpoint;
    public RequestResponder(ConnectorMessageManager messageManager, IPath path, String targetEndpoint, int requestId) {
        this._messageManager = messageManager;
        this._path = path;
        this._targetEndpoint = targetEndpoint;
        this._requestId = requestId;
    }
    public void respond(JObject json) {
        this._messageManager.sendRequestAnswer(Connector2EpsMessageType.RequestResponse, this._path, json, this._targetEndpoint, this._requestId);
    }
    public void respondWithError(ICError error) {
        this._messageManager.sendRequestAnswer(Connector2EpsMessageType.RequestResponseFailed, this._path, error.toJson(), this._targetEndpoint, this._requestId);
    }
}
