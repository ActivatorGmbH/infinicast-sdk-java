package io.infinicast.client.api.paths;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
public class ICDataQuery {
    ArrayList<ICDataFilter> _dataFilters;
    int _limit;
    int _start;
    SortCriteria _orderCriteria;
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
    public SortCriteria getOrderCriteria() {
        return this._orderCriteria;
    }
    public void setOrderCriteria(SortCriteria value) {
        this._orderCriteria = value;
    }
}
