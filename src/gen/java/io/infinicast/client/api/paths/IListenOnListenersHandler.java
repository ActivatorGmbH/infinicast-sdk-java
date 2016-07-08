package io.infinicast.client.api.paths;
import io.infinicast.*;

public interface IListenOnListenersHandler {
    void onListeningStarted(IListeningStartedContext context);
    void onListeningChanged(IListeningChangedContext context);
    void onListeningEnded(IListeningEndedContext context);
}
