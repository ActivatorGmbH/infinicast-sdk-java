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
