package io.infinicast.client.api.paths;

public interface IListenOnListenersHandler {
    void onListeningStarted(IListeningStartedContext context);
    void onListeningChanged(IListeningChangedContext context);
    void onListeningEnded(IListeningEndedContext context);
}
