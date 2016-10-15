package io.infinicast.client.protocol;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
