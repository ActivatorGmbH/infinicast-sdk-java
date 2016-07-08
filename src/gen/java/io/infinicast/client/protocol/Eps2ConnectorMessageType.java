package io.infinicast.client.protocol;
import io.infinicast.*;

public enum Eps2ConnectorMessageType {
    Message(0),
    Request(1),
    RequestResponse(2),
    RegisterHandler(3),
    JsonQueryResult(4),
    CreateChildSuccess(5),
    IntroduceObject(6),
    MessageValidate(7),
    ListAdd(8),
    ListChange(9),
    InitConnector(10),
    SetObjectData(11),
    DataChangeValidate(12),
    GetOrCreate(13),
    CreateOrUpdateRole(14),
    DestroyRole(15),
    GetRoleForPathResult(16),
    ListeningStarted(17),
    ListeningEnded(18),
    ListeningChanged(19),
    ListenTerminate(20),
    EndpointDisconnected(21),
    DebugStatistics(22),
    PathRoleSetup(23),
    Reminder(24),
    ListRemove(25),
    DebugObserverMessage(26);
    private final int value;
    public int getValue() {  return value; }
    Eps2ConnectorMessageType(int value) { this.value = value; }
}
