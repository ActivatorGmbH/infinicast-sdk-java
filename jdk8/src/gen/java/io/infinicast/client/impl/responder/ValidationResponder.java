package io.infinicast.client.impl.responder;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.handler.IValidationResponder;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.protocol.Connector2EpsMessageType;
public class ValidationResponder implements IValidationResponder {
    IEndpoint _endpointInfo;
    JObject _acceptJson;
    ConnectorMessageManager _messageManager;
    IPath _path;
    Connector2EpsMessageType _validateMessageType;
    public ValidationResponder(Connector2EpsMessageType validateMessageType, ConnectorMessageManager messageManager, JObject acceptJson, IPath path, IEndpoint endpointInfo) {
        this._validateMessageType = validateMessageType;
        this._messageManager = messageManager;
        this._acceptJson = acceptJson;
        this._path = path;
        this._endpointInfo = endpointInfo;
    }
    public void accept() {
        this._messageManager.sendValidatedMessage(this._validateMessageType, this._path, this._acceptJson, this._endpointInfo.getEndpointId());
    }
    public void correct(JObject correctedJson) {
        this._messageManager.sendValidatedMessage(this._validateMessageType, this._path, correctedJson, this._endpointInfo.getEndpointId());
    }
    public void reject() {
    }
}
