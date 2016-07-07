package io.infinicast.client.api.query;
import io.activator.infinicast.*;

public enum ListeningType {
    Any(0),
    Message(1),
    Data(2),
    Request(3);
    private final int value;
    public int getValue() {  return value; }
    ListeningType(int value) { this.value = value; }
}
