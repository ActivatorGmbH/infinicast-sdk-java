package io.infinicast.client.api.paths;

public interface IListenOnListenersHandlerWithoutChange {
    void onListeningStarted(IListeningStartedContext context);
    void onListeningEnded(IListeningEndedContext context);
}
