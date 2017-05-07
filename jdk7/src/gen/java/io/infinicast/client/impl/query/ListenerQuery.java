package io.infinicast.client.impl.query;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
    public void toList(TriConsumer<ICError, ArrayList<IEndpointAndData>, IAPathContext> callback) {
        this._executor.getListenerList(callback, this._roleFilter, this._listeningType);
    }
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    public CompletableFuture<ListenerListResult> toListAsync() {
        ListenerQuery self = this;
        final CompletableFuture<ListenerListResult> tcs = new CompletableFuture<ListenerListResult>();
        this.toList(new TriConsumer<ICError, ArrayList<IEndpointAndData>, IAPathContext>() {
            public void accept(ICError error, ArrayList<IEndpointAndData> list, IAPathContext context) {
                if (error != null) {
                    tcs.completeExceptionally(new ICException(error));
                }
                else {
                    ListenerListResult result = new ListenerListResult();
                    result.setContext(context);
                    result.setList(list);
                    tcs.complete(result);
                }
                ;
            }
        }
        );
        return tcs;
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
            public void accept(ICError error) {
                if (registrationCompleteCallback != null) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if (error != null) {
                    _executor.unhandeledError(error);
                }
                ;
            }
        }
        );
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    public CompletableFuture<Void> onStartAsync(Consumer<IListeningStartedContext> handler) {
        ListenerQuery self = this;
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onStart(handler, new CompleteCallback() {
            public void accept(ICError error) {
                if (error != null) {
                    tcs.completeExceptionally(new ICException(error));
                }
                else {
                    tcs.complete(null);
                }
                ;
            }
        }
        );
        return tcs;
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
    public CompletableFuture<Void> onDataChangeAsync(Consumer<IListeningChangedContext> handler) {
        ListenerQuery self = this;
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onDataChange(handler, new CompleteCallback() {
            public void accept(ICError error) {
                if (error != null) {
                    tcs.completeExceptionally(new ICException(error));
                }
                else {
                    tcs.complete(null);
                }
                ;
            }
        }
        );
        return tcs;
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
            public void accept(ICError error) {
                if (registrationCompleteCallback != null) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if (error != null) {
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
    public CompletableFuture<Void> onEndAsync(Consumer<IListeningEndedContext> handler) {
        ListenerQuery self = this;
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onEnd(handler, new CompleteCallback() {
            public void accept(ICError error) {
                if (error != null) {
                    tcs.completeExceptionally(new ICException(error));
                }
                else {
                    tcs.complete(null);
                }
                ;
            }
        }
        );
        return tcs;
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
            public void accept(ICError error) {
                if (registrationCompleteCallback != null) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if (error != null) {
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
            public void accept(ICError error) {
                if (registrationCompleteCallback != null) {
                    registrationCompleteCallback.accept(error);
                    ;
                }
                else if (error != null) {
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
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    public CompletableFuture<Void> liveAsync(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange) {
        ListenerQuery self = this;
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.live(onStart, onEnd, onChange, new CompleteCallback() {
            public void accept(ICError error) {
                if (error != null) {
                    tcs.completeExceptionally(new ICException(error));
                }
                else {
                    tcs.complete(null);
                }
                ;
            }
        }
        );
        return tcs;
    }
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    public CompletableFuture<Void> liveAsync(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd) {
        return this.liveAsync(onStart, onEnd, (Consumer<IListeningChangedContext>) null);
    }
}
