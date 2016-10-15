package io.infinicast.client.api.paths;
import io.infinicast.*;

import java.util.*;


import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.impl.pathAccess.*;

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
     * adds a listener that will be informed as soon as an endpoint that fits the filters will stop to listen on this path.
    */
    void onEnd(Consumer<IListeningEndedContext> handler, CompleteCallback registrationCompleteCallback);
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    void onDataChange(Consumer<IListeningChangedContext> handler);
    /**
     * adds a listener that will be informed as soon as the endpoint data of a listener on this path will be changed
    */
    void onDataChange(Consumer<IListeningChangedContext> handler, CompleteCallback registrationCompleteCallback);
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
}
