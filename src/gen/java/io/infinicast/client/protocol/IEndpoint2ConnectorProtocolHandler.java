package io.infinicast.client.protocol;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;

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
public interface IEndpoint2ConnectorProtocolHandler {
    void onInitConnector(JObject data, JObject senderEndpoint);
    void onReceiveRequestResponse(JObject data, int requestId, JObject senderEndpointObject);
    void onReceiveRequest(JObject data, String path, int requestId, JObject senderEndpointObject);
    void onReceiveJsonQueryResult(JArray list, int fullCount, int requestId);
    void onCreateChildSuccess(JObject data, String path, int requestId);
    void onIntroduceObject(JObject data, String path, JObject endpointObject);
    void onReceiveMessage(JObject data, String path, JObject endpointObject);
    void onReceiveMessageValidate(JObject data, String path, JObject endpointObject);
    void onListAdd(JObject data, String listPath, String path, JObject senderEndpointObject);
    void onListRemove(JObject data, String listPath, String path, JObject senderEndpointObject);
    void onListChange(JObject data, String listPath, String path, JObject senderEndpointObject);
    void onSetObjectData(JObject data, String path, JObject endpointObject);
    void onGetOrCreate(JObject data, String path, int requestId, boolean newlyCreated);
    void onCreateOrUpdateRole(JObject data, int requestId);
    void onDestroyRole(JObject data, int requestId);
    void onGetRoleForPathResult(JArray arr, JObject data, int requestId);
    void onListeningEnded(String path, JObject endpointObject, boolean disconnected, JObject data);
    void onListeningStarted(String path, JObject endpointObject, JObject data);
    void onListeningChanged(String path, JObject endpointObject, JObject data);
    void onDebugStatistics(JObject json, int requestId);
    void onPathRoleSetup(JObject data, int requestId);
    void onReminderTriggered(String path, JObject data);
    void onReceiveDataChangeValidate(JObject data, String path, JObject endpointObject);
    void onListenTerminate(JObject data);
    void onEndpointDisconnected(String path, JObject endpointObject);
    void onDebugObserverMessage(String path, JObject data);
}
