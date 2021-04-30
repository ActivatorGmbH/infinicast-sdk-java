package io.infinicast.client.api.paths.taskObjects;

import io.infinicast.client.impl.pathAccess.IPathAndData;

import java.util.ArrayList;
public class APListQueryResult {
    ArrayList<IPathAndData> _list;
    int _fullCount;
    public ArrayList<IPathAndData> getList() {
        return this._list;
    }
    public void setList(ArrayList<IPathAndData> value) {
        this._list = value;
    }
    public int getFullCount() {
        return this._fullCount;
    }
    public void setFullCount(int value) {
        this._fullCount = value;
    }
}
