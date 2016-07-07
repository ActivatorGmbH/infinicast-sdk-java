package io.infinicast.client.api.paths;
import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.client.api.query.OrderCriteria;
import io.infinicast.client.impl.query.ICDataFilter;
import io.activator.infinicast.*;
import java.util.*;

public class ICDataQuery {
    ArrayList<ICDataFilter> _dataFilters;
    int _limit;
    int _start;
    OrderCriteria _orderCriteria;
    public ICDataQuery() {
        this.setDataFilters(new ArrayList<ICDataFilter>());
        this.setLimit(-1);
        this.setStart(-1);
    }
    public JObject toJson() {
        JObject data = new JObject();
        JArray filters = new JArray();
        for (ICDataFilter icDataFilter : this.getDataFilters()) {
            filters.Add(icDataFilter.toJson());
        }
        data.set("filters", filters);
        if ((this.getLimit() != -1)) {
            data.set("limit", this.getLimit());
        }
        if ((this.getStart() != -1)) {
            data.set("start", this.getStart());
        }
        if ((this.getOrderCriteria() != null)) {
            data.set("order", this.getOrderCriteria().getField());
            if (this.getOrderCriteria().getIsAscending()) {
                data.set("orderAsc", this.getOrderCriteria().getIsAscending());
            }
        }
        return data;
    }
    public ArrayList<ICDataFilter> getDataFilters() {
        return this._dataFilters;
    }
    public void setDataFilters(ArrayList<ICDataFilter> value) {
        this._dataFilters = value;
    }
    public int getLimit() {
        return this._limit;
    }
    public void setLimit(int value) {
        this._limit = value;
    }
    public int getStart() {
        return this._start;
    }
    public void setStart(int value) {
        this._start = value;
    }
    public OrderCriteria getOrderCriteria() {
        return this._orderCriteria;
    }
    public void setOrderCriteria(OrderCriteria value) {
        this._orderCriteria = value;
    }
}
