package io.infinicast.client.api.query;

public class SortCriteria {
    String _field;
    boolean _isAscending;
    public static SortCriteria byField(String field, boolean isAscending) {
        SortCriteria sort = new SortCriteria();
        sort.setField(field);
        sort.setIsAscending(isAscending);
        return sort;
    }
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
    public static SortCriteria byField(String field) {
        return SortCriteria.byField(field, true);
    }
}
