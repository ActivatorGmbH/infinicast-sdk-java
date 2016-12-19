package io.infinicast.client.api.paths;

import io.infinicast.client.api.query.SortCriteria;

import java.util.ArrayList;
public class ModifyAndGetChildrenOptions extends QueryOptionsData  {
    public ModifyAndGetChildrenOptions() {
    }
    public ModifyAndGetChildrenOptions withoutData() {
        super.noData = true;
        return this;
    }
    public ModifyAndGetChildrenOptions withOrder(SortCriteria order) {
        super.order = order;
        return this;
    }
    public ModifyAndGetChildrenOptions withStart(int start) {
        super.start = start;
        return this;
    }
    public ModifyAndGetChildrenOptions withLimit(int limit) {
        super.limit = limit;
        return this;
    }
    public ModifyAndGetChildrenOptions withDataContext(String path, DataContextRelativeOptions relative) {
        if (super.dataContextPaths == null) {
            super.dataContextPaths = new ArrayList<DataContextRequest>();
        }
        DataContextRequest r = new DataContextRequest();
        r.path = path;
        r.relativeTo = relative;
        super.dataContextPaths.add(r);
        return this;
    }
    public ModifyAndGetChildrenOptions withDataContext(String path) {
        return this.withDataContext(path, DataContextRelativeOptions.Root);
    }
}
