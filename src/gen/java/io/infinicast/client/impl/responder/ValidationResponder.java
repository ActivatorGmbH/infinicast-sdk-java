package io.infinicast.client.impl.responder;
import org.joda.time.DateTime;
import io.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
