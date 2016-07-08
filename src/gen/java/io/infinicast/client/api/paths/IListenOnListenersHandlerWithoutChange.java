package io.infinicast.client.api.paths;
import io.infinicast.*;

public interface IListenOnListenersHandlerWithoutChange {
    void onListeningStarted(IListeningStartedContext context);
    void onListeningEnded(IListeningEndedContext context);
}
