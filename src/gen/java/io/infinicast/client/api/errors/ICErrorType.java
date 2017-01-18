package io.infinicast.client.api.errors;

public enum ICErrorType {
    TimedOut(0),
    RightsCheck(1),
    NoListener(2),
    CouldNotLoginWithoutCredentials(3),
    CouldNotLoginWithWrongCredentials(4),
    NoServer(5),
    InternalError(6),
    ParseFailed(7),
    Custom(8);
    private final int value;
    public int getValue() {  return value; }
    ICErrorType(int value) { this.value = value; }
}
