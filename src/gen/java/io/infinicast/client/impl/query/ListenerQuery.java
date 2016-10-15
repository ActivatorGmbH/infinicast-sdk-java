package io.infinicast.client.impl.query;
import io.infinicast.*;

import java.util.*;


import io.infinicast.client.api.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.impl.pathAccess.*;

/**
 * access to listeners on a given path.
*/
public class ListenerQuery implements IListenerQuery {
    IPath _path = null;
    ListenerQueryExecutor _executor = null;
    String _roleFilter = "";
    ListeningType _listeningType;
    public ListenerQuery(IPath path, ListenerQueryExecutor executor) {
        this._path = path;
        this._executor = executor;
        this._listeningType = ListeningType.Any;
    }
    /**
     * filters the listener query by an endpoint role
     * the query needs to be finished via e.g. ToList
    */
    public IListenerQuery filterRole(String role) {
        this._roleFilter = role;
        return this;
    }
    /**
     * filters the listener query by listening type
     * the query needs to be finished via e.g. ToList
    */
    public IListenerQuery filterType(ListeningType type) {
        this._listeningType = type;
        return this;
    }
    public ListeningType getListeningType() {
        return this._listeningType;
    }
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    public void toList(TriConsumer<ErrorInfo, ArrayList<IEndpointAndData>, IAPathContext> callback) {
        this._executor.getListenerList(callback, this._roleFilter, this._listeningType);
    }

    public String getFilteredRole() {
        return this._roleFilter;
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    public void onEnd(final Consumer<IListeningEndedContext> handler, final CompleteCallback registrationCompleteCallback) {
        ListenerQuery self = this;
        this._executor.onListeningEnded(new Consumer<IListeningEndedContext>() {
            public void accept(IListeningEndedContext context) {
                handler.accept(context);
                ;
            }
        }
        , this.getHandlerRegistrationOptions(), new CompleteCallback() {
            public void accept(ErrorInfo error) {
                if ((registrationCompleteCallback != null)) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if ((error != null)) {
                    _executor.unhandeledError(error);
                }
                ;
            }
        }
        );
    }

    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    public void onDataChange(final Consumer<IListeningChangedContext> handler) {
        this.onDataChange(handler, (CompleteCallback) null);
    }

    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    public void onDataChange(final Consumer<IListeningChangedContext> handler, final CompleteCallback registrationCompleteCallback) {
        ListenerQuery self = this;
        this._executor.onListeningChanged(new Consumer<IListeningChangedContext>() {
            public void accept(IListeningChangedContext context) {
                handler.accept(context);
                ;
            }
        }
        , this.getHandlerRegistrationOptions(), new CompleteCallback() {
            public void accept(ErrorInfo error) {
                if ((registrationCompleteCallback != null)) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if ((error != null)) {
                    _executor.unhandeledError(error);
                }
                ;
            }
        }
        );
    }

    ListeningHandlerRegistrationOptions getHandlerRegistrationOptions() {
        ListeningHandlerRegistrationOptions options = new ListeningHandlerRegistrationOptions();
        options.withRole(this.getFilteredRole());
        options.setListenerType(this.getListeningType());
        return options;
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    public void onStart(final Consumer<IListeningStartedContext> handler) {
        this.onStart(handler, (CompleteCallback) null);
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    public void onStart(final Consumer<IListeningStartedContext> handler, final CompleteCallback registrationCompleteCallback) {
        ListenerQuery self = this;
        this._executor.onListeningStarted(new Consumer<IListeningStartedContext>() {
            public void accept(IListeningStartedContext context) {
                handler.accept(context);
                ;
            }
        }
        , this.getHandlerRegistrationOptions(), new CompleteCallback() {
            public void accept(ErrorInfo error) {
                if ((registrationCompleteCallback != null)) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if ((error != null)) {
                    _executor.unhandeledError(error);
                }
                ;
            }
        }
        );
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    public void onEnd(final Consumer<IListeningEndedContext> handler) {
        this.onEnd(handler, (CompleteCallback) null);
    }
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    public void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange, final CompleteCallback registrationCompleteCallback) {
        ListenerQuery self = this;
        this._executor.getAndListenOnListeners(onStart, onChange, onEnd, this.getHandlerRegistrationOptions(), new CompleteCallback() {
            public void accept(ErrorInfo error) {
                if ((registrationCompleteCallback != null)) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if ((error != null)) {
                    _executor.unhandeledError(error);
                }
                ;
            }
        }
        );
    }
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    public void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange) {
        this.live(onStart, onEnd, onChange, (CompleteCallback) null);
    }
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    public void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd) {
        this.live(onStart, onEnd, (Consumer<IListeningChangedContext>) null, (CompleteCallback) null);
    }

}
