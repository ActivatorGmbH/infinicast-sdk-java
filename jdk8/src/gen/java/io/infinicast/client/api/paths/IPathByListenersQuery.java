package io.infinicast.client.api.paths;

import io.infinicast.QuadConsumer;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.taskObjects.PathListWithCountResult;
import io.infinicast.client.impl.pathAccess.IPathAndCount;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
/**
 * access to listeners on a given path.
*/
public interface IPathByListenersQuery {
    /**
     * starts the result list at the provided position.
     * @param position The first item to be included (0...len) default:0
    */
    IPathByListenersQuery start(int position);
    /**
     * limits the amount of results to count
     * @param count the amount of items from start to be included -1 for no limit default:-1
    */
    IPathByListenersQuery limit(int count);
    /**
     * finishs the query and returns the list of paths and listenercount of the given query
    */
    void toList(QuadConsumer<ICError, ArrayList<IPathAndCount>, IAPathContext, Integer> callback);
    /**
     * finishs the query and returns the list of paths and listenercount of the given query
    */
    CompletableFuture<PathListWithCountResult> toListAsync();
}
