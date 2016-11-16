package io.infinicast.client.api.paths;

public enum AtomicChangeType {
    Set(0),
    AddToArray(1),
    RemoveFromArray(2),
    IncValue(3),
    DecValue(4),
    AddToSet(5),
    RemoveFromSet(6),
    RemoveProperty(7);
    private final int value;
    public int getValue() {  return value; }
    AtomicChangeType(int value) { this.value = value; }
}
