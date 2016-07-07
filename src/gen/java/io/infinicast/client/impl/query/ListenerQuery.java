package io.infinicast.client.impl.query;
import io.infinicast.TriConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.paths.taskObjects.ListenerListResult;
import io.infinicast.client.api.query.ListeningType;
import io.infinicast.client.impl.pathAccess.IEndpointAndData;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;

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
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    public CompletableFuture<ListenerListResult> toListAsync() {
        CompletableFuture<ListenerListResult> tcs = new CompletableFuture<ListenerListResult>();
        this.toList((error, list, context) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                ListenerListResult result = new ListenerListResult();
                result.setContext(context);
                result.setList(list);
                tcs.complete(result);
            }
            ;
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
    public void onEnd(Consumer<IListeningEndedContext> handler, CompleteCallback registrationCompleteCallback) {
        this._executor.onListeningEnded((context) -> {
            handler.accept(context);
            ;
        }
        , this.getHandlerRegistrationOptions(), (error) -> {
            if ((registrationCompleteCallback != null)) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if ((error != null)) {
                this._executor.unhandeledError(error);
            }
            ;
        }
        );
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    public CompletableFuture<Void> onStartAsync(Consumer<IListeningStartedContext> handler) {
        CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onStart(handler, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    public void onDataChange(Consumer<IListeningChangedContext> handler) {
        this.onDataChange(handler, (CompleteCallback) null);
    }
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    public CompletableFuture<Void> onDataChangeAsync(Consumer<IListeningChangedContext> handler) {
        CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onDataChange(handler, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    public void onDataChange(Consumer<IListeningChangedContext> handler, CompleteCallback registrationCompleteCallback) {
        this._executor.onListeningChanged((context) -> {
            handler.accept(context);
            ;
        }
        , this.getHandlerRegistrationOptions(), (error) -> {
            if ((registrationCompleteCallback != null)) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if ((error != null)) {
                this._executor.unhandeledError(error);
            }
            ;
        }
        );
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    public CompletableFuture<Void> onEndAsync(Consumer<IListeningEndedContext> handler) {
        CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onEnd(handler, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
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
    public void onStart(Consumer<IListeningStartedContext> handler) {
        this.onStart(handler, (CompleteCallback) null);
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    public void onStart(Consumer<IListeningStartedContext> handler, CompleteCallback registrationCompleteCallback) {
        this._executor.onListeningStarted((context) -> {
            handler.accept(context);
            ;
        }
        , this.getHandlerRegistrationOptions(), (error) -> {
            if ((registrationCompleteCallback != null)) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if ((error != null)) {
                this._executor.unhandeledError(error);
            }
            ;
        }
        );
    }
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    public void onEnd(Consumer<IListeningEndedContext> handler) {
        this.onEnd(handler, (CompleteCallback) null);
    }
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    public void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange, CompleteCallback registrationCompleteCallback) {
        this._executor.getAndListenOnListeners(onStart, onChange, onEnd, this.getHandlerRegistrationOptions(), (error) -> {
            if ((registrationCompleteCallback != null)) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if ((error != null)) {
                this._executor.unhandeledError(error);
            }
            ;
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
        CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.live(onStart, onEnd, onChange, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
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
