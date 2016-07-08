package io.infinicast.client.api.paths;
import io.infinicast.*;

public interface IListeningEndedContext extends IPathAndEndpointContext {
    boolean wasLastListenerOfRole(String role);
    int getListenerCount(String role);
    boolean getIsDisconnected();
}
