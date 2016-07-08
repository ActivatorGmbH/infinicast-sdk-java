package io.infinicast.client.protocol.messages;
import io.infinicast.APlayStringMessage;
import io.infinicast.JObject;
import io.infinicast.StringExtensions;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.infinicast.*;

public class Connector2EpsMessage extends BaseMessage  {
    String _space;
    Connector2EpsMessageType _type;
    Connector2EpsMessageType _handlerType;
    String _version;
    public static Connector2EpsMessage parseString(APlayStringMessage stringMessage) {
        JObject data = JObject.Parse(stringMessage.getDataAsString());
        return Connector2EpsMessage.parseInternal(data);
    }
    static Connector2EpsMessage parseInternal(JObject data) {
        Connector2EpsMessage msg = new Connector2EpsMessage();
        msg._setDataByMessage(data);
        msg.setType(((Connector2EpsMessageType) Connector2EpsMessageType.valueOf(data.getString("type"))));
        if ((data.get("handlerType") != null)) {
            msg.setHandlerType(((Connector2EpsMessageType) Connector2EpsMessageType.valueOf((data.getString("handlerType")))));
        }
        return msg;
    }
    public APlayStringMessage buildStringMessage() {
        JObject result = new JObject();
        if (!(StringExtensions.IsNullOrEmpty(this.getSpace()))) {
            result.set("space", this.getSpace().toString());
        }
        result.set("type", this.getType().toString());
        if ((this.getHandlerType() != null)) {
            result.set("handlerType", this.getHandlerType().toString());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getVersion()))) {
            if ((super.getData() == null)) {
                super.setData(new JObject());
            }
            super.getData().set("version", this.getVersion());
        }
        super._fillJson(result);
        APlayStringMessage msg = new APlayStringMessage();
        msg.setDataAsString(result.toString());
        return msg;
    }
    public static Connector2EpsMessage clone(Connector2EpsMessage original) {
        Connector2EpsMessage newMsg = new Connector2EpsMessage();
        newMsg._setDataByClone(original);
        newMsg.setType(original.getType());
        newMsg.setHandlerType(original.getHandlerType());
        return newMsg;
    }
    public static Connector2EpsMessage parseInner(JObject data) {
        return Connector2EpsMessage.parseInternal(data);
    }
    public String getSpace() {
        return this._space;
    }
    public void setSpace(String value) {
        this._space = value;
    }
    public Connector2EpsMessageType getType() {
        return this._type;
    }
    public void setType(Connector2EpsMessageType value) {
        this._type = value;
    }
    public Connector2EpsMessageType getHandlerType() {
        return this._handlerType;
    }
    public void setHandlerType(Connector2EpsMessageType value) {
        this._handlerType = value;
    }
    public String getVersion() {
        return this._version;
    }
    public void setVersion(String value) {
        this._version = value;
    }
}
