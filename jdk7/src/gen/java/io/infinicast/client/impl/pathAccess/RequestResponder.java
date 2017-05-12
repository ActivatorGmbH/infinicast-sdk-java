package io.infinicast.client.impl.pathAccess;

import io.infinicast.JObject;
import io.infinicast.Logger;
import io.infinicast.LoggerFactory;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.errors.ICErrorType;
import io.infinicast.client.api.errors.ICException;
import io.infinicast.client.api.paths.handler.requests.IAPResponder;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.protocol.Connector2EpsMessageType;
public class RequestResponder implements IAPResponder {
    static Logger _logger = LoggerFactory.getLogger(RequestResponder.class);
    ConnectorMessageManager _messageManager;
    IPath _path;
    int _requestId;
    String _targetEndpoint;
    boolean didRespond = false;
    int takesTooLongCheckCount;
    public RequestResponder(ConnectorMessageManager messageManager, IPath path, String targetEndpoint, int requestId) {
        this._messageManager = messageManager;
        this._path = path;
        this._targetEndpoint = targetEndpoint;
        this._requestId = requestId;
        this.takesTooLongCheckCount = 0;
    }
    public void respond(JObject json) {
        if (this.didRespond) {
            throw new RuntimeException(new ICException(ICError.create(ICErrorType.InternalError, "Request already has been answered", this._path.toString())));
        }
        this.didRespond = true;
        this._messageManager.sendRequestAnswer(Connector2EpsMessageType.RequestResponse, this._path, json, this._targetEndpoint, this._requestId);
        RequestResponder._logger.info(((((((("response " + this._requestId) + " ") + this._path.toString()) + " endpoint: ") + this._targetEndpoint) + " data ") + " path ") + this._path.toString());
    }
    public void respondWithError(ICError error) {
        if (this.didRespond) {
            throw new RuntimeException(new ICException(ICError.create(ICErrorType.InternalError, "Request already has been answered", this._path.toString())));
        }
        this.didRespond = true;
        this._messageManager.sendRequestAnswer(Connector2EpsMessageType.RequestResponseFailed, this._path, error.toJson(), this._targetEndpoint, this._requestId);
        RequestResponder._logger.info(((((((("errorResponse " + this._requestId) + " ") + this._path.toString()) + " endpoint: ") + this._targetEndpoint) + " data ") + " path ") + this._path.toString());
    }
    public boolean alreadyResponded() {
        return this.didRespond;
    }
    public boolean answerTakesLong() {
        return ((this.takesTooLongCheckCount)++ == 2);
    }
    public String toString() {
        return ((((this._requestId + " ") + this._targetEndpoint) + " ") + this._path);
    }
    public void sendHandlingStarted() {
        this._messageManager.sendRequestAnswer(Connector2EpsMessageType.RequestHandlingStarted, this._path, null, this._targetEndpoint, this._requestId);
    }
}
