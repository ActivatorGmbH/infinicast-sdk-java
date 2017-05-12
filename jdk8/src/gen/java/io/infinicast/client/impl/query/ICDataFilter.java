package io.infinicast.client.impl.query;

import io.infinicast.JObject;
import io.infinicast.client.api.query.Filter;
public class ICDataFilter {
    int _dataFilterType;
    JObject _content;
    public static ICDataFilter fromJObject(JObject query) {
        ICDataFilter filter = new ICDataFilter();
        filter.setContent(query);
        filter.setDataFilterType(1);
        return filter;
    }
    public static ICDataFilter fromFieldFilter(Filter ff) {
        ICDataFilter filter = new ICDataFilter();
        filter.setContent(ff.toJson());
        filter.setDataFilterType(2);
        return filter;
    }
    public JObject toJson() {
        JObject json = new JObject();
        json.set("type", this.getDataFilterType());
        json.set("content", this.getContent());
        return json;
    }
    public int getDataFilterType() {
        return this._dataFilterType;
    }
    public void setDataFilterType(int value) {
        this._dataFilterType = value;
    }
    public JObject getContent() {
        return this._content;
    }
    public void setContent(JObject value) {
        this._content = value;
    }
}
