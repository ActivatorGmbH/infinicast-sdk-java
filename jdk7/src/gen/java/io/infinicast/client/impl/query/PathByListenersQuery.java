package io.infinicast.client.impl.query;

import io.infinicast.CompletableFuture;
import io.infinicast.QuadConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.errors.ICException;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.api.paths.IPathByListenersQuery;
import io.infinicast.client.api.paths.taskObjects.PathListWithCountResult;
import io.infinicast.client.impl.pathAccess.IPathAndCount;

import java.util.ArrayList;
/**
 * access to listeners on a given path.
*/
public class PathByListenersQuery implements IPathByListenersQuery {
    IPath _path = null;
    ChildrenWithListenersQueryExecutor _executor = null;
    int startPosition = -1;
    int limitPosition = -1;
    public PathByListenersQuery(IPath path, ChildrenWithListenersQueryExecutor executor) {
        this._path = path;
        this._executor = executor;
    }
    /**
     * starts the result list at the provided position.
     * @param position The first item to be included (0...len) default:0
    */
    public IPathByListenersQuery start(int position) {
        this.startPosition = position;
        return this;
    }
    /**
     * limits the amount of results to count
     * @param count the amount of items from start to be included -1 for no limit default:-1
    */
    public IPathByListenersQuery limit(int count) {
        this.limitPosition = count;
        return this;
    }
    /**
     * finishs the query and returns the list of paths and listenercount of the given query
    */
    public void toList(QuadConsumer<ICError, ArrayList<IPathAndCount>, IAPathContext, Integer> callback) {
        this._executor.getChildrenWithListeners(callback, this.startPosition, this.limitPosition);
    }
    /**
     * finishs the query and returns the list of paths and listenercount of the given query
    */
    public CompletableFuture<PathListWithCountResult> toListAsync() {
        PathByListenersQuery self = this;
        final CompletableFuture<PathListWithCountResult> tcs = new CompletableFuture<PathListWithCountResult>();
        this.toList(new QuadConsumer<ICError, ArrayList<IPathAndCount>, IAPathContext, Integer>() {
            public void accept(ICError error, ArrayList<IPathAndCount> list, IAPathContext context, Integer count) {
                if ((error != null)) {
                    tcs.completeExceptionally(new ICException(error));
                }
                else {
                    PathListWithCountResult result = new PathListWithCountResult();
                    result.setContext(context);
                    result.setList(list);
                    result.setFullCount(count);
                    tcs.complete(result);
                }
                ;
            }
        }
        );
        return tcs;
    }
}
