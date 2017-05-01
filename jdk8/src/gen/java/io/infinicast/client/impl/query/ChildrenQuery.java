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
 * by using IChildrenQuery a path can be used as a collection containing other paths.
*/
public class ChildrenQuery implements IChildrenQuery {
    ChildQueryExecutor _executor;
    ICDataQuery _dataQuery;
    IPath _path;
    public ChildrenQuery(IPath path, ChildQueryExecutor executor) {
        this._dataQuery = new ICDataQuery();
        this._path = path;
        this._executor = executor;
    }
    /**
     * filters the collection data via a Field Filter.
     * The data needs to be converted into a result by e.g. ToList();
     * example:
     * var booksForAdults = infinicast.Path("Books")
     * // Fluent Filter
     * .FilterInData(Filter.For("fromAge").Gt(18))
     * .FilterInData(Filter.For("releaseDate").Gt(DateTime.Now))
     * .ToListAsync();
    */
    public IChildrenQuery filterInData(Filter filter) {
        this._dataQuery.getDataFilters().add(ICDataFilter.fromFieldFilter(filter));
        return this;
    }
    public IChildrenQuery filterInData(JObject jsonEquals) {
        this._dataQuery.getDataFilters().add(ICDataFilter.fromJObject(jsonEquals));
        return this;
    }
    /**
     * changes the ordering of the collection query.
     * The data needs to be converted into a result by e.g. ToList();
     * @param orderCriteria
     * @return
    */
    public IChildrenQuery orderByData(SortCriteria orderCriteria) {
        this._dataQuery.setOrderCriteria(orderCriteria);
        return this;
    }
    public IChildrenQuery limit(int limit) {
        this._dataQuery.setLimit(limit);
        return this;
    }
    public IChildrenQuery start(int start) {
        this._dataQuery.setStart(start);
        return this;
    }
    /**
     * Adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    public void add(JObject objectData, CreateObjectCallback callback) {
        this._executor.addChild(objectData, null, callback);
    }
    /**
     * Searches an element in the collection that fits the previously filtered Data.
     * if no result has been returned it adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    public void addOrFindOne(JObject newObjectValue, QuadConsumer<ICError, JObject, IAPathContext, Boolean> action) {
        this._executor.findOneOrAddChild(this.getQuery(), newObjectValue, action);
    }
    /**
     * registers a handler that will be triggered when an element is added to the collection path
    */
    public void onAdd(final BiConsumer<JObject, IPathAndEndpointContext> handler) {
        this.onAdd(handler, (CompleteCallback) null);
    }
    /**
     * finishs the query and returns all element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    public void toList(APListQueryResultCallback result) {
        this._executor.findChildren(this.getQuery(), result);
    }
    /**
     * modifies the data via an atomicChange object that allows to chain multiple field based atomic operations
    */
    public void modifyAndGetData(AtomicChange data, APListQueryResultCallback callback) {
        this._executor.modifyAndGetChildrenData(this.getQuery(), data, callback);
    }
    /**
     * Adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    public CompletableFuture<ADataAndPathContext> addAsync(JObject objectData) {
        final CompletableFuture<ADataAndPathContext> tcs = new CompletableFuture<ADataAndPathContext>();
        this.add(objectData, (error, data, context) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                ADataAndPathContext result = new ADataAndPathContext();
                result.data = data;
                result.context = context;
                tcs.complete(result);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * sets the data of all filtered elements
    */
    public CompletableFuture<Integer> setDataAsync(JObject data) {
        final CompletableFuture<Integer> tcs = new CompletableFuture<Integer>();
        this.setData(data, (error, count) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(count);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * Searches an element in the collection that fits the previously filtered Data.
     * if no result has been returned it adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    public CompletableFuture<FindOneOrAddChildResult> addOrFindOneAsync(JObject newObjectValue) {
        final CompletableFuture<FindOneOrAddChildResult> tcs = new CompletableFuture<FindOneOrAddChildResult>();
        this.addOrFindOne(newObjectValue, (error, data, context, isNew) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                FindOneOrAddChildResult result = new FindOneOrAddChildResult();
                result.context = context;
                result.data = data;
                result.isNew = isNew;
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * finishs the query and returns all element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    public CompletableFuture<APListQueryResult> toListAsync() {
        final CompletableFuture<APListQueryResult> tsc = new CompletableFuture<APListQueryResult>();
        this.toList((error, list, count) -> {
            if ((error != null)) {
                tsc.completeExceptionally(new ICException(error));
            }
            else {
                APListQueryResult listResult = new APListQueryResult();
                listResult.setFullCount(count);
                listResult.setList(list);
                tsc.complete(listResult);
            }
            ;
        }
        );
        return tsc;
    }
    /**
     * modifies the data via an atomicChange object that allows to chain multiple field based atomic operations
    */
    public CompletableFuture<APListQueryResult> modifyAndGetDataAsync(AtomicChange data) {
        final CompletableFuture<APListQueryResult> tcs = new CompletableFuture<APListQueryResult>();
        this.modifyAndGetData(data, (error, list, count) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                APListQueryResult result = new APListQueryResult();
                result.setFullCount(count);
                result.setList(list);
                tcs.complete(result);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * registers a handler that will be triggered when an element is added to the collection path
    */
    public void onAdd(final BiConsumer<JObject, IPathAndEndpointContext> handler, final CompleteCallback completeCallback) {
        this._executor.onChildAdd((data, context) -> {
            handler.accept(data, context);
            ;
        }
        , null, (error) -> {
            this.useCompletionCallback(completeCallback, error);
        }
        );
    }
    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    public CompletableFuture<Integer> removeAsync() {
        final CompletableFuture<Integer> tcs = new CompletableFuture<Integer>();
        this.remove((error, count) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(count);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * finishs the query and returns the first element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    public void first(final BiConsumer<ICError, IPathAndData> result) {
        this._dataQuery.setLimit(1);
        this.toList((error, list, count) -> {
            if ((error != null)) {
                result.accept(error, null);
                ;
            }
            else {
                if (((list == null) || (list.size() < 1))) {
                    result.accept(null, null);
                    ;
                }
                else {
                    result.accept(null, list.get(0));
                    ;
                }
            }
            ;
        }
        );
    }
    /**
     * finishs the query and returns the first element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    public CompletableFuture<IPathAndData> firstAsync() {
        final CompletableFuture<IPathAndData> tcs = new CompletableFuture<IPathAndData>();
        this.first((error, ele) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(ele);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    public void remove() {
        this.remove((BiConsumer<ICError, Integer>) null);
    }
    /**
     * registers a handler that will be triggered when an element is added to the collection path
    */
    public CompletableFuture<Void> onAddAsync(BiConsumer<JObject, IPathAndEndpointContext> handler) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onAdd(handler, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    public void remove(BiConsumer<ICError, Integer> completeCallback) {
        this._executor.removeChildren(this.getQuery(), completeCallback);
    }
    /**
     * registers a handler that will be triggered when an element is changed in the collection path
    */
    public CompletableFuture<Void> onChangeAsync(BiConsumer<JObject, IPathAndEndpointContext> handler) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onChange(handler, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * sets the data of all filtered elements
    */
    public void setData(JObject data) {
        this.setData(data, (BiConsumer<ICError, Integer>) null);
    }
    /**
     * registers a handler that will be triggered when an element is deleted in the collection path
    */
    public CompletableFuture<Void> onDeleteAsync(BiConsumer<JObject, IPathAndEndpointContext> handler) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onDelete(handler, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    /**
     * sets the data of all filtered elements
    */
    public void setData(JObject data, BiConsumer<ICError, Integer> completeCallback) {
        this._executor.setChildrenData(this.getQuery(), data, completeCallback);
    }
    /**
     * registers handlers for add, remove and change to the given collection path.
     * the add handler will be triggered for all elements that fit the Filter query.
    */
    public CompletableFuture<Void> liveAsync(BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onRemove, BiConsumer<JObject, IPathAndEndpointContext> onChange) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.live(onAdd, onRemove, onChange, (error) -> {
            if ((error != null)) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        );
        return tcs;
    }
    void useCompletionCallback(CompleteCallback completeCallback, ICError icError) {
        if ((completeCallback != null)) {
            completeCallback.accept(icError);
            ;
        }
        else if ((icError != null)) {
            this._executor.unhandeledError(icError);
        }
    }
    public ICDataQuery getQuery() {
        if ((this._dataQuery == null)) {
            return new ICDataQuery();
        }
        return this._dataQuery;
    }
    /**
     * registers a handler that will be triggered when an element is changed in the collection path
    */
    public void onChange(final BiConsumer<JObject, IPathAndEndpointContext> handler, final CompleteCallback completeCallback) {
        this._executor.onChildChange((data, context) -> {
            handler.accept(data, context);
            ;
        }
        , null, (error) -> {
            this.useCompletionCallback(completeCallback, error);
        }
        );
    }
    /**
     * registers a handler that will be triggered when an element is changed in the collection path
    */
    public void onChange(final BiConsumer<JObject, IPathAndEndpointContext> handler) {
        this.onChange(handler, (CompleteCallback) null);
    }
    /**
     * registers a handler that will be triggered when an element is deleted in the collection path
    */
    public void onDelete(final BiConsumer<JObject, IPathAndEndpointContext> handler, final CompleteCallback completeCallback) {
        this._executor.onChildDelete((data, context) -> {
            handler.accept(data, context);
            ;
        }
        , null, (error) -> {
            this.useCompletionCallback(completeCallback, error);
        }
        );
    }
    /**
     * registers a handler that will be triggered when an element is deleted in the collection path
    */
    public void onDelete(final BiConsumer<JObject, IPathAndEndpointContext> handler) {
        this.onDelete(handler, (CompleteCallback) null);
    }
    /**
     * registers handlers for add, remove and change to the given collection path.
     * the add handler will be triggered for all elements that fit the Filter query.
    */
    public void live(BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onRemove, BiConsumer<JObject, IPathAndEndpointContext> onChange, final CompleteCallback completeCallback) {
        this._executor.getAndListenOnChilden(this.getQuery(), (((onAdd == null) && (onChange == null)) && (onRemove == null)), onAdd, onChange, onRemove, false, false, (error) -> {
            this.useCompletionCallback(completeCallback, error);
        }
        );
    }
    /**
     * registers handlers for add, remove and change to the given collection path.
     * the add handler will be triggered for all elements that fit the Filter query.
    */
    public void live(BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onRemove, BiConsumer<JObject, IPathAndEndpointContext> onChange) {
        this.live(onAdd, onRemove, onChange, (CompleteCallback) null);
    }
}
