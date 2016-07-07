package io.activator.infinicast.client.protocol.messages;
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
public class Eps2ConnectorMessage extends BaseMessage  {
    Eps2ConnectorMessageType _type;
    JObject _endpointObject;
    Integer _fullCount;
    Boolean _newlyCreated;
    Integer _errorCode;
    JObject _errorData;
    Boolean _disconnected;
    JArray _list;
    public static Eps2ConnectorMessage parseString(APlayStringMessage message) {
        JObject data = JObject.Parse(message.getDataAsString());
        return Eps2ConnectorMessage.parseFromJson(data);
    }
    static Eps2ConnectorMessage parseFromJson(JObject data) {
        Eps2ConnectorMessage result = new Eps2ConnectorMessage();
        result._setDataByMessage(data);
        result.setType((Eps2ConnectorMessageType) Eps2ConnectorMessageType.valueOf(data.getString("type")));
        if ((data.get("endpointObject") != null)) {
            result.setEndpointObject((data.getJObject("endpointObject")));
        }
        if ((data.get("fullCount") != null)) {
            result.setFullCount((data.getInt("fullCount")));
        }
        if ((data.get("newlyCreated") != null)) {
            result.setNewlyCreated((data.getBoolean("newlyCreated")));
        }
        if ((data.get("errorCode") != null)) {
            result.setErrorCode((data.getInt("errorCode")));
        }
        if ((data.get("errorData") != null)) {
            result.setErrorData((data.getJObject("errorData")));
        }
        if ((data.get("disconnected") != null)) {
            result.setDisconnected((data.getBoolean("disconnected")));
        }
        if ((data.get("list") != null)) {
            result.setList((data.getJArray("list")));
        }
        return result;
    }
    public APlayStringMessage buildStringMessage() {
        JObject result = new JObject();
        super._fillJson(result);
        result.set("type", this.getType().toString());
        if ((this.getEndpointObject() != null)) {
            result.set("Endpoint", this.getEndpointObject());
        }
        if ((this.getFullCount() != null)) {
            result.set("fullCount", this.getFullCount());
        }
        if ((this.getNewlyCreated() != null)) {
            result.set("newlyCreated", this.getNewlyCreated());
        }
        if ((this.getErrorCode() != null)) {
            result.set("errorCode", this.getErrorCode());
        }
        if ((this.getErrorData() != null)) {
            result.set("errorData", this.getErrorData());
        }
        if ((this.getDisconnected() != null)) {
            result.set("disconnected", this.getDisconnected());
        }
        if ((this.getList() != null)) {
            result.set("list", this.getList());
        }
        APlayStringMessage msg = new APlayStringMessage();
        msg.setDataAsString(result.toString());
        return msg;
    }
    public static Eps2ConnectorMessage mapFromConnectorMessage(Connector2EpsMessage msg) {
        return Eps2ConnectorMessage.parseString(msg.buildStringMessage());
    }
    public static Eps2ConnectorMessage parseInner(JObject data) {
        return Eps2ConnectorMessage.parseFromJson(data);
    }
    public Eps2ConnectorMessageType getType() {
        return this._type;
    }
    public void setType(Eps2ConnectorMessageType value) {
        this._type = value;
    }
    public JObject getEndpointObject() {
        return this._endpointObject;
    }
    public void setEndpointObject(JObject value) {
        this._endpointObject = value;
    }
    public Integer getFullCount() {
        return this._fullCount;
    }
    public void setFullCount(Integer value) {
        this._fullCount = value;
    }
    public Boolean getNewlyCreated() {
        return this._newlyCreated;
    }
    public void setNewlyCreated(Boolean value) {
        this._newlyCreated = value;
    }
    public Integer getErrorCode() {
        return this._errorCode;
    }
    public void setErrorCode(Integer value) {
        this._errorCode = value;
    }
    public JObject getErrorData() {
        return this._errorData;
    }
    public void setErrorData(JObject value) {
        this._errorData = value;
    }
    public Boolean getDisconnected() {
        return this._disconnected;
    }
    public void setDisconnected(Boolean value) {
        this._disconnected = value;
    }
    public JArray getList() {
        return this._list;
    }
    public void setList(JArray value) {
        this._list = value;
    }
}
