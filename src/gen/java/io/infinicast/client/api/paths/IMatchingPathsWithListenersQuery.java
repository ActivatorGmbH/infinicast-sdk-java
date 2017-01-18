package io.infinicast.client.api.paths;

import io.infinicast.TriConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.taskObjects.PathListResult;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
/**
 * access to listeners on a given path.
*/
public interface IMatchingPathsWithListenersQuery {
    /**
     * starts the result list at the provided position.
    */
    IMatchingPathsWithListenersQuery start(int position);
    /**
     * limits the amount of results to count
    */
    IMatchingPathsWithListenersQuery limit(int count);
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    void toList(TriConsumer<ICError, ArrayList<IPath>, IAPathContext> callback);
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    CompletableFuture<PathListResult> toListAsync();
}
