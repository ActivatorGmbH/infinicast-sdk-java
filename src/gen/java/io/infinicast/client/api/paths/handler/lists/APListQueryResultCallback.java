package io.infinicast.client.api.paths.handler.lists;

import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.impl.pathAccess.IPathAndData;

import java.util.ArrayList;
@FunctionalInterface
public interface APListQueryResultCallback {
    void accept(ErrorInfo error, ArrayList<IPathAndData> resultList, int fullCount);
}
