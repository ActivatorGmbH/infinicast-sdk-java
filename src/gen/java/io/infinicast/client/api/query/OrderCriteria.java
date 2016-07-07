package io.infinicast.client.api.query;
import io.activator.infinicast.*;

public class OrderCriteria {
    String _field;
    boolean _isAscending;
    public String getField() {
        return this._field;
    }
    public void setField(String value) {
        this._field = value;
    }
    public boolean getIsAscending() {
        return this._isAscending;
    }
    public void setIsAscending(boolean value) {
        this._isAscending = value;
    }
}
