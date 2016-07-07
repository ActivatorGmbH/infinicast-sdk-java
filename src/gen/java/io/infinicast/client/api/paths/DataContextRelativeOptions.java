package io.infinicast.client.api.paths;
import io.activator.infinicast.*;

public enum DataContextRelativeOptions {
    Root(0),
    SenderEndpoint(1);
    private final int value;
    public int getValue() {  return value; }
    DataContextRelativeOptions(int value) { this.value = value; }
}
