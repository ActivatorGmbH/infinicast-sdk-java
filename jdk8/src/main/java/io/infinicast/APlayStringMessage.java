package io.infinicast;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Created by Michael on 31.07.2015.
 */
public class APlayStringMessage implements Serializable, IMessage {

    protected static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private String payload;
    private byte[] compressedData;
    private JObject decodedData;
    private boolean binary = true;

    public JObject getDataAsDecodedJson() {
        if (decodedData == null) {
            decodedData = (JObject) JObject.Parse(payload);
        }
        return decodedData;
    }

    public void setBinary() {
        binary = true;
    }

    public void setDataAsString(String str) {
        payload = str;
        decodedData = null;
    }

    public void setDataAsJson(JObject json) {
        decodedData = json;
        payload = null;
    }

    public String getDataAsString() {
        if (payload != null) {
            return payload;
        }
        if (decodedData != null) {
            payload = decodedData.toString();
        }
        return payload;
    }

    /*@Override
    public void doCompression() {
        String payload = getDataAsString();
        try {
            byte[] zipped = StringCompressor.compress(payload);
            this.compressedData = zipped;
        } catch (IOException e) {
        }
    }*/

    public boolean isBinary() {
        return binary;
    }

    /*   @Override
       public byte[] getCompressedData() {
           return compressedData;
       }
   */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof APlayStringMessage))
            return false;
        APlayStringMessage otherMessage = (APlayStringMessage) other;

        return (this.getDataAsString() == null && otherMessage.getDataAsString() == null) || (this.getDataAsString() != null && this
                .getDataAsString().equals(otherMessage.getDataAsString()));
    }
}

