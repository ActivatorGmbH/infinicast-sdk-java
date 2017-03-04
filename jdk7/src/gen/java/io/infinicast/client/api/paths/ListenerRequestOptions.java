package io.infinicast.client.api.paths;

public class ListenerRequestOptions extends ListenerRequestOptionsData  {
    public ListenerRequestOptions() {
    }
    public ListenerRequestOptions withRole(String role_) {
        super.role = role_;
        return this;
    }
}
