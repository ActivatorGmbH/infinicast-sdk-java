package io.infinicast.client.impl.pathAccess;

import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.handler.requests.IAPResponder;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.protocol.Connector2EpsMessageType;
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
