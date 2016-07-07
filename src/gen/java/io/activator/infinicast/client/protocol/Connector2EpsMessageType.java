package io.activator.infinicast.client.protocol;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
public enum Connector2EpsMessageType {
    RequestResponse(0),
    Request(1),
    InitConnector(2),
    JsonQuery(3),
    CreateChildRequest(4),
    SetObjectData(5),
    IntroduceObject(6),
    Message(7),
    MessageValidate(8),
    MessageValidated(9),
    ListAdd(10),
    ListRemove(11),
    ListChange(12),
    RemoveHandlers(13),
    RegisterHandler(14),
    CreateOrUpdateRole(15),
    DestroyRole(16),
    ModifyRoleForPath(17),
    GetRoleForPath(18),
    GetOrCreate(19),
    ListeningEnded(20),
    ListeningStarted(21),
    DeleteFromCollection(22),
    DebugPingInfo(23),
    DebugInfoMessage(24),
    DebugStatistics(25),
    PathRoleSetup(26),
    SetDebugName(27),
    GetObjectData(28),
    GetListeningList(29),
    UpdateData(30),
    SetChildData(31),
    ModifyChildData(32),
    RemoveChildren(33),
    GetEndpointConnectionInfo(34),
    Reminder(35),
    AddReminder(36),
    DeleteReminder(37),
    GetAndListenOnChildren(38),
    DataListenUpdate(39),
    ListeningChanged(40),
    GetAndListenOnListeners(41),
    ListenTerminate(42),
    SystemCommand(43),
    DataChangeValidate(44),
    DataChangeValidated(45),
    EndpointDisconnected(46),
    DebugObserverMessage(47);
    private final int value;
    public int getValue() {  return value; }
    Connector2EpsMessageType(int value) { this.value = value; }
}
