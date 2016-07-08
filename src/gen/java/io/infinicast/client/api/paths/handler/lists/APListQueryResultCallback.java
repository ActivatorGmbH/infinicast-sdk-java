package io.infinicast.client.api.paths.handler.lists;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.impl.pathAccess.IPathAndData;
import io.infinicast.*;
import java.util.*;

@FunctionalInterface
public interface APListQueryResultCallback {
    void accept(ErrorInfo error, ArrayList<IPathAndData> resultList, int fullCount);
}
