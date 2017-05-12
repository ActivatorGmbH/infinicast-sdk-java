package io.infinicast.client.api.paths;

public interface IListeningStartedContext extends IPathAndEndpointContext {
    boolean isFirstListenerOfRole(String role);
    int getListenerCount(String role);
}
