package io.infinicast.client.api.paths;

public interface IListeningEndedContext extends IPathAndEndpointContext {
    boolean wasLastListenerOfRole(String role);
    int getListenerCount(String role);
    boolean getIsDisconnected();
}
