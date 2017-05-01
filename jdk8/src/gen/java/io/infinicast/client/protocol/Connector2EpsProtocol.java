package io.infinicast.client.protocol;
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
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
public class Connector2EpsProtocol {
    public Connector2EpsMessage encodeInitConnector(String space, String type, JObject credentials) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType((Connector2EpsMessageType.InitConnector));
        msg.setRole((type));
        msg.setSpace(space);
        if ((credentials != null)) {
            msg.setData(new JObject());
            msg.getData().set("credentials", credentials);
        }
        msg.setVersion(VersionHelper.getClientVersion());
        return msg;
    }
    public Connector2EpsMessage encodeRegisterHandlerMessage(Connector2EpsMessageType messageType, String path, int requestId, Boolean consomeOnePerRole, Boolean sticky, ListeningType listeningType, String roleFilter, boolean terminationHandler) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType((Connector2EpsMessageType.RegisterHandler));
        msg.setRequestId(requestId);
        msg.setHandlerType((messageType));
        msg.setPath((path));
        JObject settings = null;
        if (((consomeOnePerRole != null) && (consomeOnePerRole == true))) {
            if ((settings == null)) {
                settings = new JObject();
            }
            settings.set("once", true);
            if (((sticky != null) && (sticky == true))) {
                settings.set("sticky", true);
            }
        }
        if ((listeningType != ListeningType.Any)) {
            if ((settings == null)) {
                settings = new JObject();
            }
            settings.set("listenerType", listeningType.toString());
        }
        if (!(StringExtensions.IsNullOrEmpty(roleFilter))) {
            if ((settings == null)) {
                settings = new JObject();
            }
            settings.set("role", roleFilter);
        }
        if (terminationHandler) {
            if ((settings == null)) {
                settings = new JObject();
            }
            settings.set("terminationHandler", true);
        }
        msg.setData(settings);
        return msg;
    }
    public Connector2EpsMessage encodeMessage(Connector2EpsMessageType messageType, String path, JObject jsonData) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType((messageType));
        if (!(StringExtensions.IsNullOrEmpty(path))) {
            msg.setPath((path));
        }
        msg.setData((jsonData));
        return msg;
    }
    public Connector2EpsMessage encodeValidatedMessage(Connector2EpsMessageType messageType, String path, JObject jsonData, String originalEndpoint) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType(messageType);
        if (!(StringExtensions.IsNullOrEmpty(path))) {
            msg.setPath((path));
        }
        msg.setData((jsonData));
        msg.setOriginalEndpoint((originalEndpoint));
        return msg;
    }
    public Connector2EpsMessage encodeMessageWithRequestId(Connector2EpsMessageType messageType, String path, JObject jsonData, String answerTarget, int requestId) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType((messageType));
        if (!(StringExtensions.IsNullOrEmpty(path))) {
            msg.setPath((path));
        }
        msg.setData((jsonData));
        msg.setRequestId((requestId));
        msg.setAnswerTarget((answerTarget));
        return msg;
    }
    public Connector2EpsMessage encodeRemoveHandlerMessage(String path, Connector2EpsMessageType handlerType, int messageRequestId, String endpoint) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType((Connector2EpsMessageType.RemoveHandlers));
        msg.setPath((path));
        msg.setRequestId(messageRequestId);
        msg.setHandlerType(handlerType);
        msg.setEndpoint(endpoint);
        return msg;
    }
    public Connector2EpsMessage encodeMessageWithResponse(Connector2EpsMessageType messageType, String path, JObject jsonData, int messageRequestId) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType((messageType));
        if (!(StringExtensions.IsNullOrEmpty(path))) {
            msg.setPath((path));
        }
        msg.setData((jsonData));
        msg.setRequestId((messageRequestId));
        return msg;
    }
    public Connector2EpsMessage encodeDebugPingInfo(String path, int pingInMs) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType(Connector2EpsMessageType.DebugPingInfo);
        JObject ob = new JObject();
        ob.set("ping", pingInMs);
        msg.setPath(path);
        msg.setData(ob);
        return msg;
    }
    public Connector2EpsMessage encodeDebugMesssageInfo(String path, int level, JObject json) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg.setType(Connector2EpsMessageType.DebugInfoMessage);
        JObject ob = new JObject();
        ob.set("level", level);
        ob.set("info", json);
        msg.setPath(path);
        msg.setData(ob);
        return msg;
    }
}
