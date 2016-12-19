package io.infinicast.client.api.paths;

import java.util.function.Consumer;
public class ListenOnListenersHandler implements IListenOnListenersHandler {
    Consumer<IListeningStartedContext> _OnListeningStarted;
    Consumer<IListeningChangedContext> _OnListeningChanged;
    Consumer<IListeningEndedContext> _OnListeningEnded;
    public ListenOnListenersHandler() {
        this._OnListeningStarted = null;
        this._OnListeningChanged = null;
        this._OnListeningEnded = null;
    }
    public ListenOnListenersHandler withOnListeningStartedHandler(Consumer<IListeningStartedContext> onAdd) {
        this._OnListeningStarted = onAdd;
        return this;
    }
    public ListenOnListenersHandler withOnListeningChangedHandler(Consumer<IListeningChangedContext> onChange) {
        this._OnListeningChanged = onChange;
        return this;
    }
    public ListenOnListenersHandler withListeningEndedHandler(Consumer<IListeningEndedContext> onRemove) {
        this._OnListeningEnded = onRemove;
        return this;
    }
    public void onListeningStarted(IListeningStartedContext context) {
        if (this._OnListeningStarted != null) {
            this._OnListeningStarted.accept(context);
            ;
        }
    }
    public void onListeningChanged(IListeningChangedContext context) {
        if (this._OnListeningChanged != null) {
            this._OnListeningChanged.accept(context);
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
