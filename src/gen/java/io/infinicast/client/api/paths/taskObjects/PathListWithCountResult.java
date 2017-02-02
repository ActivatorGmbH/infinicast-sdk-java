package io.infinicast.client.api.paths.taskObjects;

import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.impl.pathAccess.IPathAndCount;

import java.util.ArrayList;
public class PathListWithCountResult {
    ArrayList<IPathAndCount> _list;
    IAPathContext _context;
    int _fullCount;
    public ArrayList<IPathAndCount> getList() {
        return this._list;
    }
    public void setList(ArrayList<IPathAndCount> value) {
        this._list = value;
    }
    public IAPathContext getContext() {
        return this._context;
    }
    public void setContext(IAPathContext value) {
        this._context = value;
    }
    public int getFullCount() {
        return this._fullCount;
    }
    public void setFullCount(int value) {
        this._fullCount = value;
    }
}
