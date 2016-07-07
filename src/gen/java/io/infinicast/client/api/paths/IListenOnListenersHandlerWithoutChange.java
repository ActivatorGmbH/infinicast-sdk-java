package io.infinicast.client.api.paths;
import io.activator.infinicast.*;

public interface IListenOnListenersHandlerWithoutChange {
    void onListeningStarted(IListeningStartedContext context);
    void onListeningEnded(IListeningEndedContext context);
}
