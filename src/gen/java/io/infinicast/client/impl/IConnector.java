package io.infinicast.client.impl;
import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.impl.objectState.ObjectStateManager;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.*;

public interface IConnector {
    PathImpl getRootPath();
    IPath path(String path);
    ObjectStateManager getObjectStateManager();
    void onInitConnector(JObject data, JObject senderEndpoint);
    String getRole();
    String getSpace();
    void triggerDisconnect();
    void unhandeledError(IPath iaPath, JObject errorJson);
    void unhandeledErrorInfo(IPath iaPath, ErrorInfo errorJson);
    JObject getCredentials();
}
