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
