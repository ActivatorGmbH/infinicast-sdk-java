package io.infinicast.client.api.paths;
import io.infinicast.*;
import java.util.*;

public class ListeningHandlerRegistrationOptions extends ListeningHandlerRegistrationOptionsData  {
    public ListeningHandlerRegistrationOptions() {
    }
    public ListeningHandlerRegistrationOptions withSendingEndpointDataContext() {
        super.sendingEndpointDataContext = true;
        return this;
    }
    public ListeningHandlerRegistrationOptions withDataContext(String path, DataContextRelativeOptions relative) {
        if ((super.dataContextPaths == null)) {
            super.dataContextPaths = new ArrayList<DataContextRequest>();
        }
        DataContextRequest r = new DataContextRequest();
        r.path = path;
        r.relativeTo = relative;
        super.dataContextPaths.add(r);
        return this;
    }
    public ListeningHandlerRegistrationOptionsData withRole(String role) {
        super.setRoleFilter(role);
        return this;
    }
    public ListeningHandlerRegistrationOptions oncePerRole() {
        super.setIsOncePerRole(true);
        return this;
    }
    public ListeningHandlerRegistrationOptions withDataContext(String path) {
        return this.withDataContext(path, DataContextRelativeOptions.Root);
    }
}
