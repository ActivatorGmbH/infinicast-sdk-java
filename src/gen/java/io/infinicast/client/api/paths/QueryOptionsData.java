package io.infinicast.client.api.paths;

import io.infinicast.client.api.query.OrderCriteria;

import java.util.ArrayList;
public class QueryOptionsData {
    public boolean noData = false;
    public OrderCriteria order = null;
    public int start = 0;
    public int limit = 0;
    public ArrayList<DataContextRequest> dataContextPaths = null;
    public QueryOptionsData() {
    }
}
