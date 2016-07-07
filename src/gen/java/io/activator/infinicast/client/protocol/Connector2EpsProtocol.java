package io.activator.infinicast.client.protocol;
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
