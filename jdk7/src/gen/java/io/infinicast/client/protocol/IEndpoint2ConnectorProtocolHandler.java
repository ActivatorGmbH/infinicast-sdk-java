package io.infinicast.client.protocol;

import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
public interface IEndpoint2ConnectorProtocolHandler {
    void onInitConnector(ICError error, JObject data, JObject senderEndpoint);
    void onReceiveRequestResponse(ICError error, JObject data, int requestId, JObject senderEndpointObject);
    void onReceiveRequest(ICError error, JObject data, String path, int requestId, JObject senderEndpointObject);
    void onReceiveJsonQueryResult(ICError error, JArray list, int fullCount, int requestId);
    void onCreateChildSuccess(ICError error, JObject data, String path, int requestId);
    void onIntroduceObject(ICError error, JObject data, String path, JObject endpointObject);
    void onReceiveMessage(ICError error, JObject data, String path, JObject endpointObject);
    void onReceiveMessageValidate(ICError error, JObject data, String path, JObject endpointObject);
    void onListAdd(ICError error, JObject data, String listPath, String path, JObject senderEndpointObject);
    void onListRemove(ICError error, JObject data, String listPath, String path, JObject senderEndpointObject);
    void onListChange(ICError error, JObject data, String listPath, String path, JObject senderEndpointObject);
    void onSetObjectData(ICError error, JObject data, String path, JObject endpointObject);
    void onGetOrCreate(ICError error, JObject data, String path, int requestId, boolean newlyCreated);
    void onCreateOrUpdateRole(ICError error, JObject data, int requestId);
    void onDestroyRole(ICError error, JObject data, int requestId);
    void onGetRoleForPathResult(ICError error, JArray arr, JObject data, int requestId);
    void onListeningEnded(ICError error, String path, JObject endpointObject, boolean disconnected, JObject data);
    void onListeningStarted(ICError error, String path, JObject endpointObject, JObject data);
    void onListeningChanged(ICError error, String path, JObject endpointObject, JObject data);
    void onDebugStatistics(ICError error, JObject json, int requestId);
    void onPathRoleSetup(ICError error, JObject data, int requestId);
    void onReminderTriggered(ICError error, String path, JObject data);
    void onReceiveDataChangeValidate(ICError error, JObject data, String path, JObject endpointObject);
    void onListenTerminate(ICError error, JObject data);
    void onEndpointDisconnected(ICError error, String path, JObject endpointObject);
    void onDebugObserverMessage(ICError error, String path, JObject data);
}
