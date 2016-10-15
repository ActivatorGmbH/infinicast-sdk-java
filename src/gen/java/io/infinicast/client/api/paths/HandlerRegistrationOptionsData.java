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
public class HandlerRegistrationOptionsData {
    public boolean sendingEndpointDataContext = false;
    public ArrayList<DataContextRequest> dataContextPaths = null;
    boolean _isOncePerRole;
    boolean _isSticky;
    ListeningType _listenerType;
    String _roleFilter;
    public HandlerRegistrationOptionsData() {
    }
    public boolean getIsOncePerRole() {
        return this._isOncePerRole;
    }
    public void setIsOncePerRole(boolean value) {
        this._isOncePerRole = value;
    }
    public boolean getIsSticky() {
        return this._isSticky;
    }
    public void setIsSticky(boolean value) {
        this._isSticky = value;
    }
    public ListeningType getListenerType() {
        return this._listenerType;
    }
    public void setListenerType(ListeningType value) {
        this._listenerType = value;
    }
    public String getRoleFilter() {
        return this._roleFilter;
    }
    public void setRoleFilter(String value) {
        this._roleFilter = value;
    }
}
