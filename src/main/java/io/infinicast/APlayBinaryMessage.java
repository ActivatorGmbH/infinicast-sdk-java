package io.infinicast;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by ocean on 10.01.2017.
 */
public class APlayBinaryMessage implements Serializable {

    private int NULL_VALUE = Integer.MIN_VALUE;
    private byte[] data;
    private DataOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    private DataInputStream inputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof APlayBinaryMessage))
            return false;
        APlayBinaryMessage otherMessage = (APlayBinaryMessage) other;

        return Arrays.equals(getBinaryBytes(), otherMessage.getBinaryBytes());
    }


    public void startWriting() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new DataOutputStream(byteArrayOutputStream);
    }


    public void startReading(byte[] data) {
        this.data = data;
        byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new DataInputStream(byteArrayInputStream);
    }


    public int binarySize() {

        return getBinaryBytes().length;
    }


    public byte[] getBinaryBytes() {

        if (outputStream != null) {
            data = byteArrayOutputStream.toByteArray();
            outputStream = null;
        }
        return data;
    }


    public String readString() {
        try {
            int n = inputStream.readInt();
            if (n == 0) {
                return null;
            }
            byte[] bytes = new byte[n];
            inputStream.read(bytes, 0, n);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            handleIoException(e);
        }
        return null;
    }


    public JObject readJson() {

        int size = this.readInt();
        if (size < 0) {
            return null;
        }
        JObject jObject = new JObject();
        for (int i = 0; i < size; i++) {
            String key = this.readString();
            jObject.set(key, this.readTypeDecoded());
        }
        return jObject;
    }


    public Object readTypeDecoded() {
        APlayBinaryMessage msg = this;
        byte type = msg.readByte();

        Object value = null;
        switch (type) {
            case 0:
                value = null;
                break;
            case 1:
                value = msg.readBoolean();
                break;
            case 2:
                value = msg.readJArray();
                break;
            case 3:
                value = msg.readInt();
                break;
            case 4:
                value = msg.readJson();
                break;
            case 5:
                value = msg.readString();
                break;
            case 6:
                value = msg.readByte();
                break;
            case 7:
                value = msg.readLong();
                break;
            case 8:
                value = msg.readFloat();
                break;
            case 9:
                value = msg.readDouble();
                break;
        }
        return value;
    }


    public void writeTypeEncoded(Object value) {
        APlayBinaryMessage msg = this;
        if (value == null) {
            msg.writeByte((byte) 0);
        } else if (value instanceof Boolean) {
            msg.writeByte((byte) 1);
            msg.writeBoolean((Boolean) value);
        } else if (value instanceof JArray) {
            msg.writeByte((byte) 2);
            msg.writeJArray((JArray) value);
        } else if (value instanceof Integer) {
            msg.writeByte((byte) 3);
            msg.writeInt((Integer) value);
        } else if (value instanceof JObject) {
            msg.writeByte((byte) 4);
            msg.writeJson((JObject) value);
        } else if (value instanceof String) {
            msg.writeByte((byte) 5);
            msg.writeString((String) value);
        } else if (value instanceof Byte) {
            msg.writeByte((byte) 6);
            msg.writeByte((Byte) value);
        } else if (value instanceof Long) {
            msg.writeByte((byte) 7);
            msg.writeLong((Long) value);
        } else if (value instanceof Float) {
            msg.writeByte((byte) 8);
            msg.writeFloat((Float) value);
        } else if (value instanceof Double) {
            msg.writeByte((byte) 9);
            msg.writeDouble((Double) value);
        }
    }


    public byte readByte() {
        try {
            return inputStream.readByte();
        } catch (IOException e) {
            handleIoException(e);
        }
        return 0;
    }


    public Integer readInt() {
        try {
            int val = inputStream.readInt();
            if (val == NULL_VALUE)
                return null;
            return val;
        } catch (IOException e) {
            handleIoException(e);
        }
        return null;
    }


    public Long readLong() {
        try {
            long val = inputStream.readLong();
            if (val == NULL_VALUE)
                return null;
            return val;
        } catch (IOException e) {
            handleIoException(e);
        }
        return null;
    }

    public Float readFloat() {
        try {
            float val = inputStream.readFloat();
            if (val == NULL_VALUE)
                return null;
            return val;
        } catch (IOException e) {
            handleIoException(e);
        }
        return null;
    }

    public Double readDouble() {
        try {
            double val = inputStream.readDouble();
            if (val == NULL_VALUE)
                return null;
            return val;
        } catch (IOException e) {
            handleIoException(e);
        }
        return null;
    }

    public Boolean readBoolean() {
        try {
            byte val = inputStream.readByte();
            if (val == 0)
                return null;
            return val == 2;
        } catch (IOException e) {
            handleIoException(e);
        }
        return null;
    }


    public JArray readJArray() {
        int size = this.readInt();
        if (size < 0) {
            return null;
        }
        JArray jArray = new JArray();
        for (int i = 0; i < size; i++) {
            jArray.add(this.readTypeDecoded());
        }

        return jArray;
    }


    public void writeString(String role) {
        try {
            if (role == null) {
                outputStream.writeInt(0);
            } else {
                byte[] bytes = role.getBytes(StandardCharsets.UTF_8);
                outputStream.writeInt(bytes.length);
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            handleIoException(e);
        }
    }


    public void writeJson(JObject data) {
        if (data == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(data.getKeys().size());
            for (String key : data.getKeys()) {
                this.writeString(key);
                this.writeTypeEncoded(data.get(key));
            }
        }
    }


    public void writeByte(byte b) {
        try {
            outputStream.writeByte(b);
        } catch (IOException e) {
            handleIoException(e);
        }
    }


    public void writeInt(Integer requestId) {

        try {
            if (requestId == null) {
                outputStream.writeInt(NULL_VALUE);
            } else {
                outputStream.writeInt(requestId);
            }
        } catch (IOException e) {
            handleIoException(e);
        }
    }


    public void writeLong(Long requestId) {

        try {
            if (requestId == null) {
                outputStream.writeLong(NULL_VALUE);
            } else {
                outputStream.writeLong(requestId);
            }
        } catch (IOException e) {
            handleIoException(e);
        }
    }

    public void writeFloat(Float requestId) {
        try {
            if (requestId == null) {
                outputStream.writeFloat(NULL_VALUE);
            } else {
                outputStream.writeFloat(requestId);
            }
        } catch (IOException e) {
            handleIoException(e);
        }
    }

    public void writeDouble(Double requestId) {
        try {
            if (requestId == null) {
                outputStream.writeDouble(NULL_VALUE);
            } else {
                outputStream.writeDouble(requestId);
            }
        } catch (IOException e) {
            handleIoException(e);
        }
    }

    public void writeBoolean(Boolean newlyCreated) {
        try {
            if (newlyCreated == null) {
                outputStream.writeByte(0);
            } else {
                outputStream.writeByte(newlyCreated ? 2 : 1);
            }
        } catch (IOException e) {
            handleIoException(e);
        }
    }


    public void writeJArray(JArray data) {
        if (data == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(data.size());
            for (int i = 0; i < data.size(); i++) {
                this.writeTypeEncoded(data.get(i));
            }
        }
    }


    public boolean readOptional() {
        return readBoolean();
    }


    public boolean writeOptional(Object data) {
        boolean val = data != null;
        writeBoolean(val);
        return val;
    }

    private void handleIoException(IOException e) {

        e.printStackTrace();
        //logger.logGlobalError("exception in message streams", e);
    }

}