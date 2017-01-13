package io.infinicast.client.impl.query;

import io.infinicast.TriConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.AfinityException;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.IMatchingPathsWithListenersQuery;
import io.infinicast.client.api.paths.taskObjects.PathListResult;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
/**
 * access to listeners on a given path.
*/
public class MatchingPathsWithListenersQuery implements IMatchingPathsWithListenersQuery {
    IPath _path = null;
    ChildrenWithListenersQueryExecutor _executor = null;
    int startPosition = -1;
    int limitPosition = -1;
    public MatchingPathsWithListenersQuery(IPath path, ChildrenWithListenersQueryExecutor executor) {
        this._path = path;
        this._executor = executor;
    }
    /**
     * starts the result list at the provided position.
    */
    public IMatchingPathsWithListenersQuery start(int position) {
        this.startPosition = position;
        return this;
    }
    /**
     * limits the amount of results to count
    */
    public IMatchingPathsWithListenersQuery limit(int count) {
        this.limitPosition = count;
        return this;
    }
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    public void toList(TriConsumer<ErrorInfo, ArrayList<IPath>, IAPathContext> callback) {
        this._executor.getChildrenWithListeners(callback, this.startPosition, this.limitPosition);
    }
    /**
     * finishs the query and returns the list of listeners on a given path filtered by role or type filters.
    */
    public CompletableFuture<PathListResult> toListAsync() {
        final CompletableFuture<PathListResult> tcs = new CompletableFuture<PathListResult>();
        this.toList((error, list, context) -> {
            if (error != null) {
                tcs.completeExceptionally(new AfinityException(error));
            }
            else {
                PathListResult result = new PathListResult();
                result.setContext(context);
                result.setList(list);
                tcs.complete(result);
            }
            ;
        });
        return tcs;
    }
}
