package io.infinicast.client.impl.query;
import io.infinicast.*;

import java.util.*;


import io.infinicast.client.api.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.impl.pathAccess.*;

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
    public IChildrenQuery orderByData(OrderCriteria orderCriteria) {
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
    public void addOrFindOne(JObject newObjectValue, QuadConsumer<ErrorInfo, JObject, IAPathContext, Boolean> action) {
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
     * registers a handler that will be triggered when an element is added to the collection path
    */
    public void onAdd(final BiConsumer<JObject, IPathAndEndpointContext> handler, final CompleteCallback completeCallback) {
        ChildrenQuery self = this;
        this._executor.onChildAdd(new APListAddCallback() {
            public void accept(JObject data, IPathAndEndpointContext context) {
                handler.accept(data, context);
                ;
            }
        }
        , null, new CompleteCallback() {
            public void accept(ErrorInfo error) {
                useCompletionCallback(completeCallback, error);
            }
        }
        );
    }

    /**
     * finishs the query and returns the first element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    public void first(final BiConsumer<ErrorInfo, IPathAndData> result) {
        ChildrenQuery self = this;
        this._dataQuery.setLimit(1);
        this.toList(new APListQueryResultCallback() {
            public void accept(ErrorInfo error, ArrayList<IPathAndData> list, int count) {
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
        }
        );
    }

    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    public void delete() {
        this.delete((BiConsumer<ErrorInfo, Integer>) null);
    }

    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    public void delete(BiConsumer<ErrorInfo, Integer> completeCallback) {
        this._executor.removeChildren(this.getQuery(), completeCallback);
    }

    /**
     * sets the data of all filtered elements
    */
    public void setData(JObject data) {
        this.setData(data, (BiConsumer<ErrorInfo, Integer>) null);
    }

    /**
     * sets the data of all filtered elements
    */
    public void setData(JObject data, BiConsumer<ErrorInfo, Integer> completeCallback) {
        this._executor.setChildrenData(this.getQuery(), data, completeCallback);
    }

    void useCompletionCallback(CompleteCallback completeCallback, ErrorInfo error) {
        if ((completeCallback != null)) {
            completeCallback.accept(error);
            ;
        }
        else if ((error != null)) {
            this._executor.unhandeledError(error);
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
        ChildrenQuery self = this;
        this._executor.onChildChange(new APListAddCallback() {
            public void accept(JObject data, IPathAndEndpointContext context) {
                handler.accept(data, context);
                ;
            }
        }
        , null, new CompleteCallback() {
            public void accept(ErrorInfo error) {
                useCompletionCallback(completeCallback, error);
            }
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
        ChildrenQuery self = this;
        this._executor.onChildDelete(new APListAddCallback() {
            public void accept(JObject data, IPathAndEndpointContext context) {
                handler.accept(data, context);
                ;
            }
        }
        , null, new CompleteCallback() {
            public void accept(ErrorInfo error) {
                useCompletionCallback(completeCallback, error);
            }
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
        ChildrenQuery self = this;
        this._executor.getAndListenOnChilden(this.getQuery(), (((onAdd == null) && (onChange == null)) && (onRemove == null)), onAdd, onChange, onRemove, false, false, new CompleteCallback() {
            public void accept(ErrorInfo error) {
                useCompletionCallback(completeCallback, error);
            }
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
