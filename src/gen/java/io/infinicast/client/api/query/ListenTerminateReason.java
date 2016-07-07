package io.infinicast.client.api.query;
import io.activator.infinicast.*;

public enum ListenTerminateReason {
    UserRemoved(0),
    RoleRemoved(1);
    private final int value;
    public int getValue() {  return value; }
    ListenTerminateReason(int value) { this.value = value; }
}
