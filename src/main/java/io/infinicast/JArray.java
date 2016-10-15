package io.infinicast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.util.Iterator;

public class JArray extends JToken implements Iterable<JToken> {
    public JArray() {
        super(new ArrayNode(JsonNodeFactory.instance));
    }

    public JArray(JArray rhs) {
        this(rhs.node);
    }

    public JArray(JsonNode node) {
        super(node);
    }

    @Override
    public Iterator<JToken> iterator() {
        ArrayNode a = (ArrayNode) node;

        return new JsonNodeToJTokenIterator(a.iterator());
    }

    class JsonNodeToJTokenIterator implements Iterator<JToken> {
        private final Iterator<JsonNode> iterator;

        public JsonNodeToJTokenIterator(Iterator<JsonNode> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public JToken next() {
            return new JToken(iterator.next());
        }

        @Override
        public void remove() {

        }
    }

    public void Add(JObject rhs) {
        getArrayNode().add(rhs.node);
    }

    public void add(String o) {
        getArrayNode().add(o);
    }

    public void add(int o) {
        getArrayNode().add(o);
    }

    public void add(double o) {
        getArrayNode().add(o);
    }

    public void add(boolean o) {
        getArrayNode().add(o);
    }

    public void add(JObject rhs) {
        getArrayNode().add(rhs.node);
    }

    public void add(JArray rhs) {
        getArrayNode().add(rhs.node);
    }

    public void remove(int index) {
        getArrayNode().remove(index);
    }

    private ArrayNode getArrayNode() {
        return (ArrayNode) super.node;
    }

    public int getCount() {
        return getArrayNode().size();
    }

    public int size() {
        return getArrayNode().size();
    }

    public Object get(int index) {
        JsonNode jsonNode = node.get(index);

        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        if (jsonNode.isArray()) {
            return getJArray(index);
        }
        if (jsonNode.isObject()) {
            return getJObject(index);
        }
        if (jsonNode.isBoolean()) {
            return getBoolean(index);
        }
        if (jsonNode.isDouble() || jsonNode.isFloat()) {
            return getDouble(index);
        }
        if (jsonNode.isInt()) {
            return getInt(index);
        }
        if (jsonNode.isLong()) {
            return getLong(index);
        }
        if (jsonNode.isTextual()) {
            return getString(index);
        }
        return null;
    }

    public int getInt(int key) {
        return node.get(key).asInt();
    }

    public long getLong(int key) {
        return node.get(key).asLong();
    }

    public boolean getBoolean(int key) {
        return node.get(key).asBoolean();
    }

    public double getDouble(int key) {
        return node.get(key).asDouble();
    }

    public JArray getJArray(int key) {
        return new JArray(node.get(key));
    }

    public String getString(int key) {
        JsonNode innerNode = node.get(key);
        return null == innerNode ? null : innerNode.asText();
    }

    public JObject getJObject(int key) {
        JsonNode innerNode = this.node.get(key);
        if (null == innerNode)
            return null;
        return new JObject(innerNode);
    }

}





