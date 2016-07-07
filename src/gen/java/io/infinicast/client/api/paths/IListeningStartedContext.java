package io.infinicast.client.api.paths;
import io.activator.infinicast.*;

public interface IListeningStartedContext extends IPathAndEndpointContext {
    boolean isFirstListenerOfRole(String role);
    int getListenerCount(String role);
}
