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
public class ListenOnDataHandler implements IListenOnDataHandler {
    BiConsumer<JObject, IPath> _onAdd;
    BiConsumer<JObject, IPath> _onChange;
    BiConsumer<JObject, IPath> _onRemove;
    public ListenOnDataHandler() {
        this._onAdd = null;
        this._onChange = null;
        this._onRemove = null;
    }
    public ListenOnDataHandler withAddHandler(BiConsumer<JObject, IPath> onAdd) {
        this._onAdd = onAdd;
        return this;
    }
    public ListenOnDataHandler withChangeHandler(BiConsumer<JObject, IPath> onChange) {
        this._onChange = onChange;
        return this;
    }
    public ListenOnDataHandler withRemoveHandler(BiConsumer<JObject, IPath> onRemove) {
        this._onRemove = onRemove;
        return this;
    }
    public void onAdd(JObject data, IPath context) {
        if ((this._onAdd != null)) {
            this._onAdd.accept(data, context);
            ;
        }
    }
    public void onChange(JObject data, IPath context) {
        if ((this._onChange != null)) {
            this._onChange.accept(data, context);
            ;
        }
    }
    public void onRemove(JObject data, IPath context) {
        if ((this._onRemove != null)) {
            this._onRemove.accept(data, context);
            ;
        }
    }
}
