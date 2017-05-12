package io.infinicast.client.api;

import java.util.ArrayList;
public class EndpointSubscription {
    String _path;
    ArrayList<String> _roles;
    public String getPath() {
        return this._path;
    }
    public void setPath(String value) {
        this._path = value;
    }
    public ArrayList<String> getRoles() {
        return this._roles;
    }
    public void setRoles(ArrayList<String> value) {
        this._roles = value;
    }
}
