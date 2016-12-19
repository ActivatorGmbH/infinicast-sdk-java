package io.infinicast.client.api.query;

public enum ListenTerminateReason {
    UserRemoved(0),
    RoleRemoved(1),
    PathDeleted(2);
    private final int value;
    public int getValue() {  return value; }
    ListenTerminateReason(int value) { this.value = value; }
}
