package io.infinicast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JObject extends JToken {
    public JObject() {
        super();
    }

    public JObject(JObject rhs) {
        this(rhs.node);
    }

    JObject(JsonNode node) {
        super(node);
    }

    public static JObject Parse(String rhs) {
        ObjectNode node = JsonHelpers.parse(rhs);
        return new JObject(node);
    }

    public boolean containsKey(String key) {
        return node.get(key) != null;
    }

    public Collection<String> getKeys() {

        Iterator<String> stringIterator = node.fieldNames();
        Set<String> col = new HashSet<>();
        while (stringIterator.hasNext()) {
            col.add(stringIterator.next());
        }
        return col;
    }


}



