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
        if ((this._OnListeningStarted != null)) {
            this._OnListeningStarted.accept(context);
            ;
        }
    }
    public void onListeningChanged(IListeningChangedContext context) {
        if ((this._OnListeningChanged != null)) {
            this._OnListeningChanged.accept(context);
            ;
        }
    }
    public void onListeningEnded(IListeningEndedContext context) {
        if ((this._OnListeningEnded != null)) {
            this._OnListeningEnded.accept(context);
            ;
        }
    }
}
