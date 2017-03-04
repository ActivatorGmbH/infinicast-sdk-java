package io.infinicast.client.api.paths;

import java.util.ArrayList;
public class GetDataOptions extends GetDataOptionsData  {
    public GetDataOptions() {
    }
    public GetDataOptions withDataContext(String path, DataContextRelativeOptions relative) {
        if (super.dataContextPaths == null) {
            super.dataContextPaths = new ArrayList<DataContextRequest>();
        }
        DataContextRequest r = new DataContextRequest();
        r.path = path;
        r.relativeTo = relative;
        super.dataContextPaths.add(r);
        return this;
    }
    public GetDataOptions withDataContext(String path) {
        return this.withDataContext(path, DataContextRelativeOptions.Root);
    }
}
