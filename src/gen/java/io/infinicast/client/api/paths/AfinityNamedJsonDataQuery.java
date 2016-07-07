package io.infinicast.client.api.paths;
import io.activator.infinicast.*;

public class AfinityNamedJsonDataQuery {
    ICDataQuery _query;
    String _name;
    public ICDataQuery getQuery() {
        return this._query;
    }
    public void setQuery(ICDataQuery value) {
        this._query = value;
    }
    public String getName() {
        return this._name;
    }
    public void setName(String value) {
        this._name = value;
    }
}
