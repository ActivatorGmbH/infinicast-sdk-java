package io.infinicast.client.api.paths;
import io.activator.infinicast.*;

public interface IListenOnListenersHandler {
    void onListeningStarted(IListeningStartedContext context);
    void onListeningChanged(IListeningChangedContext context);
    void onListeningEnded(IListeningEndedContext context);
}
