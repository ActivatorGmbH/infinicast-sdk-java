package io.infinicast.client.api.paths;
import io.infinicast.client.api.query.ListeningType;
import io.infinicast.*;
import java.util.*;

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
