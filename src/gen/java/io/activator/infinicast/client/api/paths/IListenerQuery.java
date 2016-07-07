package io.activator.infinicast.client.api.paths;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
/**
 * access to listeners on a given path.
*/
public interface IListenerQuery {
    /**
     * filters the listener query by an endpoint role
     * the query needs to be finished via e.g. ToList
    */
    IListenerQuery filterRole(String role);
    /**
     * filters the listener query by listening type
     * the query needs to be finished via e.g. ToList
    */
    IListenerQuery filterType(ListeningType type);
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    void toList(TriConsumer<ErrorInfo, ArrayList<IEndpointAndData>, IAPathContext> callback);
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    CompletableFuture<ListenerListResult> toListAsync();
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    void onEnd(Consumer<IListeningEndedContext> handler, CompleteCallback registrationCompleteCallback);
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    CompletableFuture<Void> onStartAsync(Consumer<IListeningStartedContext> handler);
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    void onDataChange(Consumer<IListeningChangedContext> handler);
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    CompletableFuture<Void> onDataChangeAsync(Consumer<IListeningChangedContext> handler);
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    void onDataChange(Consumer<IListeningChangedContext> handler, CompleteCallback registrationCompleteCallback);
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    CompletableFuture<Void> onEndAsync(Consumer<IListeningEndedContext> handler);
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    void onStart(Consumer<IListeningStartedContext> handler);
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will begin to listen on this path.
    */
    void onStart(Consumer<IListeningStartedContext> handler, CompleteCallback registrationCompleteCallback);
    /**
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    void onEnd(Consumer<IListeningEndedContext> handler);
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange, CompleteCallback registrationCompleteCallback);
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange);
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    void live(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd);
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    CompletableFuture<Void> liveAsync(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd, Consumer<IListeningChangedContext> onChange);
    /**
     * adds listeners to start, end and change of endpoint listeners on this path.
     * the onStart event will be triggered for all already existing listeners on this path.
    */
    CompletableFuture<Void> liveAsync(Consumer<IListeningStartedContext> onStart, Consumer<IListeningEndedContext> onEnd);
}
