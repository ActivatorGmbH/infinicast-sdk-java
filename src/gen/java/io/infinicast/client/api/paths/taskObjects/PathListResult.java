package io.infinicast.client.api.paths.taskObjects;

import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.IAPathContext;

import java.util.ArrayList;
public class PathListResult {
    ArrayList<IPath> _list;
    IAPathContext _context;
    public ArrayList<IPath> getList() {
        return this._list;
    }
    public void setList(ArrayList<IPath> value) {
        this._list = value;
    }
    public IAPathContext getContext() {
        return this._context;
    }
    public void setContext(IAPathContext value) {
        this._context = value;
    }
}
