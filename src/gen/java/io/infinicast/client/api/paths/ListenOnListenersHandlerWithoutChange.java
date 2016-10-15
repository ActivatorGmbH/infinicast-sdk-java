package io.infinicast.client.api.paths;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;

import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
        if ((this._OnListeningStarted != null)) {
            this._OnListeningStarted.accept(context);
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
