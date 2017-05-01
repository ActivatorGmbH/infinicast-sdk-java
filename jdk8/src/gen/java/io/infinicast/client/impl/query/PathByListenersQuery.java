package io.infinicast.client.impl.query;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
        final CompletableFuture<PathListWithCountResult> tcs = new CompletableFuture<PathListWithCountResult>();
        this.toList((error, list, context, count) -> {
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
        );
        return tcs;
    }
}
