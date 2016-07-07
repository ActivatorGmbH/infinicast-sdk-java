package io.activator.infinicast.client.impl.responder;
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
