package io.activator.infinicast;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Created by Michael on 31.07.2015.
 */
public class APlayStringMessage implements Serializable, IMessage {
    protected static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private String payload;
    //private JObject decodedData;
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof APlayStringMessage))
            return false;
        APlayStringMessage otherMessage = (APlayStringMessage) other;
        return (this.getDataAsString() == null && otherMessage.getDataAsString() == null) || (this.getDataAsString() != null
                && this.getDataAsString().equals(otherMessage.getDataAsString()));
    }
    public String getDataAsString() {
        return payload;
    }
    /*
    @Override
    public JObject getDataAsDecodedJson() {
        if (decodedData == null) {
            decodedData = JsonHelpers.parse(payload);
        }
        return decodedData;
    }
    */
    public void setDataAsString(String str) {
        payload = str;
    }
    @Override
    public String toString() {
        return "APlayStringMessage{" +
                "payload='" + payload + '\'' +
                '}';
    }
}

