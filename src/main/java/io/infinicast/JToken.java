package io.infinicast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;

import java.util.ArrayList;

public class JToken {
    protected final JsonNode node;

    public JToken() {
        this(JsonHelpers.createObject());
    }

    protected JToken(JsonNode node) {
        this.node = node;
    }

    public void set(String key, Object value) {
        if (value == null) {
            set(key, (String) null);
        } else if (value instanceof String) {
            set(key, (String) value);
        } else if (value instanceof Double) {
            set(key, (double) value);
        } else if (value instanceof Float) {
            set(key, (float) value);
        } else if (value instanceof Integer) {
            set(key, (int) value);
        } else if (value instanceof Long) {
            set(key, (long) value);
        } else if (value instanceof JArray) {
            set(key, (JArray) value);
        } else if (value instanceof Boolean) {
            set(key, (boolean) value);
        } else if (value instanceof DateTime) {
            set(key, (DateTime) value);
        } else if (value instanceof JObject) {
            set(key, (JObject) value);
        } else {
            set(key, value.toString());
        }
    }

    public void set(String key, String value) {
        ((ObjectNode) this.node).put(key, value);
    }

    public void set(String key, double value) {
        ((ObjectNode) this.node).put(key, value);
    }

    public void set(String key, float value) {
        ((ObjectNode) this.node).put(key, value);
    }

    public void set(String key, int value) {
        ((ObjectNode) this.node).put(key, value);
    }

    public void set(String key, long value) {
        ((ObjectNode) this.node).put(key, value);
    }

    public void set(String key, boolean value) {
        ((ObjectNode) this.node).put(key, value);
    }

    public void set(String key, JObject value) {
        ((ObjectNode) this.node).put(key, ((JToken) value).node);
    }

    public void set(String key, JArray value) {
        ((ObjectNode) this.node).put(key, ((JToken) value).node);
    }

    public void set(String key, DateTime value) {
        set(key, value.getMillis());
    }

    public int getInt(String key) {
        return node.get(key).asInt();
    }

    public long getLong(String key) {
        return node.get(key).asLong();
    }

    public boolean getBoolean(String key) {
        return node.get(key).asBoolean();
    }

    public double getDouble(String key) {
        return node.get(key).asDouble();
    }

    public JArray getJArray(String key) {
        return new JArray(node.get(key));
    }

    public String getString(String key) {
        JsonNode innerNode = node.get(key);
        return null == innerNode ? null : innerNode.asText();
    }

    public JObject getJObject(String key) {
        JsonNode innerNode = this.node.get(key);
        if (null == innerNode)
            return null;
        return new JObject(innerNode);
    }

    public Object get(String key) {
        JsonNode jsonNode = node.get(key);
        return convertNode(key, jsonNode);
    }

    private Object convertNode(String key, JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        if (jsonNode.isArray()) {
            return getJArray(key);
        }
        if (jsonNode.isObject()) {
            return getJObject(key);
        }
        if (jsonNode.isBoolean()) {
            return getBoolean(key);
        }
        if (jsonNode.isDouble() || jsonNode.isFloat()) {
            return getDouble(key);
        }
        if (jsonNode.isInt()) {
            return getInt(key);
        }
        if (jsonNode.isLong()) {
            return getLong(key);
        }
        if (jsonNode.isTextual()) {
            return getString(key);
        }
        return null;
    }

    public String toString() {
        if (null == node)
            return this.getClass().toString() + " with suspicious null node";

        if (node.getNodeType().equals(JsonNodeType.STRING)) {
            return node.asText();
        } else {
            return node.toString();
        }
    }

    public boolean containsNonNull(String key) {
        JsonNode subNode = node.get(key);
        if (null == subNode)
            return false;
        switch (subNode.getNodeType()) {
        case ARRAY:
            return 0 < subNode.size();
        case OBJECT:
            return 0 < subNode.size();
        case STRING:
            return 0 < subNode.asText().length();
        case NULL:
            return false;
        case MISSING:
            return false;
        default:
            return true;
        }
    }

    public ArrayList<String> getStringArray(String key) {
        ArrayList<String> result = new ArrayList<String>();
        JArray array= getJArray(key);
        for (JToken t : array) {
            result.add(t.toString());
        }
        return result;
    }

}




