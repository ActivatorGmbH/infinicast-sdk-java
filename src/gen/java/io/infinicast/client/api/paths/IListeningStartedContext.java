package io.infinicast.client.api.paths;
import io.infinicast.*;

public interface IListeningStartedContext extends IPathAndEndpointContext {
    boolean isFirstListenerOfRole(String role);
    int getListenerCount(String role);
}
