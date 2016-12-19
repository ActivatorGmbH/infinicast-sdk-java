package io.infinicast.client.api.paths;

import java.util.ArrayList;
public class HandlerRegistrationOptions extends HandlerRegistrationOptionsData  {
    public HandlerRegistrationOptions() {
    }
    public HandlerRegistrationOptions stickyOncePerRole() {
        super.setIsOncePerRole(true);
        super.setIsSticky(true);
        return this;
    }
    public HandlerRegistrationOptions oncePerRole() {
        super.setIsOncePerRole(true);
        return this;
    }
    public HandlerRegistrationOptions withSendingEndpointDataContext() {
        super.sendingEndpointDataContext = true;
        return this;
    }
    public HandlerRegistrationOptions withDataContext(String path, DataContextRelativeOptions relative) {
        if (super.dataContextPaths == null) {
            super.dataContextPaths = new ArrayList<DataContextRequest>();
        }
        DataContextRequest r = new DataContextRequest();
        r.path = path;
        r.relativeTo = relative;
        super.dataContextPaths.add(r);
        return this;
    }
    public HandlerRegistrationOptions withDataContext(String path) {
        return this.withDataContext(path, DataContextRelativeOptions.Root);
    }
}
