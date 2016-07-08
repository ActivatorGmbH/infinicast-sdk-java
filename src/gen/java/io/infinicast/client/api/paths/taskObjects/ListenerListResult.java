package io.infinicast.client.api.paths.taskObjects;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.impl.pathAccess.IEndpointAndData;
import io.infinicast.*;
import java.util.*;

public class ListenerListResult {
    ArrayList<IEndpointAndData> _list;
    IAPathContext _context;
    public ArrayList<IEndpointAndData> getList() {
        return this._list;
    }
    public void setList(ArrayList<IEndpointAndData> value) {
        this._list = value;
    }
    public IAPathContext getContext() {
        return this._context;
    }
    public void setContext(IAPathContext value) {
        this._context = value;
    }
}
