package io.infinicast.client.impl;

import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.impl.objectState.ObjectStateManager;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.impl.responder.RequestResponseManager;
public interface IConnector {
    PathImpl getRootPath();
    IPath path(String path);
    ObjectStateManager getObjectStateManager();
    void onInitConnector(ICError error, JObject data, JObject senderEndpoint);
    String getRole();
    String getSpace();
    void triggerDisconnect();
    void unhandeledError(IPath iaPath, JObject errorJson);
    void unhandeledErrorInfo(IPath iaPath, ICError icErrorJson);
    JObject getCredentials();
    void receivedPing(int msgLastRoundTrip, long msgSendTime);
    RequestResponseManager getRequestResponseManager();
}
