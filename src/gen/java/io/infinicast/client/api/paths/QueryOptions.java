package io.infinicast.client.api.paths;
import io.infinicast.client.api.query.OrderCriteria;
import io.infinicast.*;
import java.util.*;

public class QueryOptions extends QueryOptionsData  {
    public QueryOptions() {
    }
    public QueryOptions withoutData() {
        super.noData = true;
        return this;
    }
    public QueryOptions withOrder(OrderCriteria order) {
        super.order = order;
        return this;
    }
    public QueryOptions withStart(int start) {
        super.start = start;
        return this;
    }
    public QueryOptions withLimit(int limit) {
        super.limit = limit;
        return this;
    }
    public QueryOptions withDataContext(String path, DataContextRelativeOptions relative) {
        if ((super.dataContextPaths == null)) {
            super.dataContextPaths = new ArrayList<DataContextRequest>();
        }
        DataContextRequest r = new DataContextRequest();
        r.path = path;
        r.relativeTo = relative;
        super.dataContextPaths.add(r);
        return this;
    }
    public QueryOptions withDataContext(String path) {
        return this.withDataContext(path, DataContextRelativeOptions.Root);
    }
}
