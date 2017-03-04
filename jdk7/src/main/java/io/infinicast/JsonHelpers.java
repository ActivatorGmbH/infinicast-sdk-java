package io.infinicast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonHelpers {
    public static ObjectNode parse(String rhs) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = mapper.readValue(rhs, ObjectNode.class);
            return node;
        } catch (IOException e) {
        }
        return null;
    }

    public static ObjectNode createObject() {
        return new ObjectNode(JsonNodeFactory.instance);
    }
}



