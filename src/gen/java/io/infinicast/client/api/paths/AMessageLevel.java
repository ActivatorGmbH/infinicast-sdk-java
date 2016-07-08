package io.infinicast.client.api.paths;
import io.infinicast.*;

public enum AMessageLevel {
    Error(0);
    private final int value;
    public int getValue() {  return value; }
    AMessageLevel(int value) { this.value = value; }
}
