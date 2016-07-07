package io.infinicast.client.api.paths;
import io.activator.infinicast.*;

public enum AfinityJsonDataQueryType {
    All(0),
    EqualsJson(1);
    private final int value;
    public int getValue() {  return value; }
    AfinityJsonDataQueryType(int value) { this.value = value; }
}
