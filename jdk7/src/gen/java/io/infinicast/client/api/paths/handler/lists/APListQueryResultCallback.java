package io.infinicast.client.api.paths.handler.lists;

import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.impl.pathAccess.IPathAndData;

import java.util.ArrayList;

public interface APListQueryResultCallback {
    void accept(ICError icError, ArrayList<IPathAndData> resultList, int fullCount);
}
