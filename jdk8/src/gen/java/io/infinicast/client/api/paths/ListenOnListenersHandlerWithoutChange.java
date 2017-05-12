package io.infinicast.client.api.paths;

import java.util.function.Consumer;
public class ListenOnListenersHandlerWithoutChange implements IListenOnListenersHandlerWithoutChange {
    Consumer<IListeningStartedContext> _OnListeningStarted;
    Consumer<IListeningEndedContext> _OnListeningEnded;
    public ListenOnListenersHandlerWithoutChange() {
        this._OnListeningStarted = null;
        this._OnListeningEnded = null;
    }
    public ListenOnListenersHandlerWithoutChange withOnListeningStartedHandler(Consumer<IListeningStartedContext> onAdd) {
        this._OnListeningStarted = onAdd;
        return this;
    }
    public ListenOnListenersHandlerWithoutChange withListeningEndedHandler(Consumer<IListeningEndedContext> onRemove) {
        this._OnListeningEnded = onRemove;
        return this;
    }
    public void onListeningStarted(IListeningStartedContext context) {
        if (this._OnListeningStarted != null) {
            this._OnListeningStarted.accept(context);
            ;
        }
    }
    public void onListeningEnded(IListeningEndedContext context) {
        if (this._OnListeningEnded != null) {
            this._OnListeningEnded.accept(context);
            ;
        }
    }
}
