package io.infinicast.client.api.paths;

import io.infinicast.JObject;
import io.infinicast.QuadConsumer;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.handler.lists.APListQueryResultCallback;
import io.infinicast.client.api.paths.handler.objects.CreateObjectCallback;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.paths.taskObjects.ADataAndPathContext;
import io.infinicast.client.api.paths.taskObjects.APListQueryResult;
import io.infinicast.client.api.paths.taskObjects.FindOneOrAddChildResult;
import io.infinicast.client.api.query.Filter;
import io.infinicast.client.api.query.SortCriteria;
import io.infinicast.client.impl.pathAccess.IPathAndData;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
/**
 * by using IChildrenQuery a path can be used as a collection containing other paths.
*/
public interface IChildrenQuery {
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
    IChildrenQuery filterInData(Filter filter);
    /**
     * filters the collection data via a json equals. Only elements of the collection that contain all of the json elements will be contained.
     * The data needs to be converted into a result by e.g. ToList();
    */
    IChildrenQuery filterInData(JObject equalsJson);
    /**
     * limits the result count
     * The data needs to be converted into a result by e.g. ToList();
    */
    IChildrenQuery limit(int count);
    /**
     * skips the first 'count' results
     * The data needs to be converted into a result by e.g. ToList();
    */
    IChildrenQuery start(int count);
    /**
     * changes the ordering of the collection query.
     * The data needs to be converted into a result by e.g. ToList();
     * @param orderCriteria
     * @return
    */
    IChildrenQuery orderByData(SortCriteria orderCriteria);
    /**
     * Adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    void add(JObject objectData, CreateObjectCallback callback);
    /**
     * Adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    CompletableFuture<ADataAndPathContext> addAsync(JObject objectData);
    /**
     * Searches an element in the collection that fits the previously filtered Data.
     * if no result has been returned it adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    void addOrFindOne(JObject newObjectValue, QuadConsumer<ICError, JObject, IAPathContext, Boolean> action);
    /**
     * Searches an element in the collection that fits the previously filtered Data.
     * if no result has been returned it adds a child object to the collection containing the passed data.
     * The newly added element will get a generated path id.
    */
    CompletableFuture<FindOneOrAddChildResult> addOrFindOneAsync(JObject newObjectValue);
    /**
     * registers a handler that will be triggered when an element is added to the collection path
    */
    void onAdd(BiConsumer<JObject, IPathAndEndpointContext> handler);
    /**
     * sets the data of all filtered elements
    */
    CompletableFuture<Integer> setDataAsync(JObject data);
    /**
     * modifies the data via an atomicChange object that allows to chain multiple field based atomic operations
    */
    void modifyAndGetData(AtomicChange data, APListQueryResultCallback callback);
    /**
     * modifies the data via an atomicChange object that allows to chain multiple field based atomic operations
    */
    CompletableFuture<APListQueryResult> modifyAndGetDataAsync(AtomicChange data);
    /**
     * registers a handler that will be triggered when an element is added to the collection path
    */
    void onAdd(BiConsumer<JObject, IPathAndEndpointContext> handler, CompleteCallback completeCallback);
    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    CompletableFuture<Integer> removeAsync();
    /**
     * finishs the query and returns the first element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    void first(BiConsumer<ICError, IPathAndData> result);
    /**
     * finishs the query and returns the first element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    CompletableFuture<IPathAndData> firstAsync();
    /**
     * finishs the query and returns all element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    void toList(APListQueryResultCallback result);
    /**
     * finishs the query and returns all element that fits the filtered query or null if no element is found
     * a callback handler or primise can be used
    */
    CompletableFuture<APListQueryResult> toListAsync();
    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    void remove();
    /**
     * registers a handler that will be triggered when an element is added to the collection path
    */
    CompletableFuture<Void> onAddAsync(BiConsumer<JObject, IPathAndEndpointContext> handler);
    /**
     * delets the elements fitting the filtered query and returns the amount of deleted elements or an error
    */
    void remove(BiConsumer<ICError, Integer> completeCallback);
    /**
     * registers a handler that will be triggered when an element is changed in the collection path
    */
    CompletableFuture<Void> onChangeAsync(BiConsumer<JObject, IPathAndEndpointContext> handler);
    /**
     * sets the data of all filtered elements
    */
    void setData(JObject data);
    /**
     * registers a handler that will be triggered when an element is deleted in the collection path
    */
    CompletableFuture<Void> onDeleteAsync(BiConsumer<JObject, IPathAndEndpointContext> handler);
    /**
     * sets the data of all filtered elements
    */
    void setData(JObject data, BiConsumer<ICError, Integer> completeCallback);
    /**
     * registers handlers for add, remove and change to the given collection path.
     * the add handler will be triggered for all elements that fit the Filter query.
    */
    CompletableFuture<Void> liveAsync(BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onRemove, BiConsumer<JObject, IPathAndEndpointContext> onChange);
    /**
     * registers a handler that will be triggered when an element is changed in the collection path
    */
    void onChange(BiConsumer<JObject, IPathAndEndpointContext> handler, CompleteCallback completeCallback);
    /**
     * registers a handler that will be triggered when an element is changed in the collection path
    */
    void onChange(BiConsumer<JObject, IPathAndEndpointContext> handler);
    /**
     * registers a handler that will be triggered when an element is deleted in the collection path
    */
    void onDelete(BiConsumer<JObject, IPathAndEndpointContext> handler, CompleteCallback completeCallback);
    /**
     * registers a handler that will be triggered when an element is deleted in the collection path
    */
    void onDelete(BiConsumer<JObject, IPathAndEndpointContext> handler);
    /**
     * registers handlers for add, remove and change to the given collection path.
     * the add handler will be triggered for all elements that fit the Filter query.
    */
    void live(BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onRemove, BiConsumer<JObject, IPathAndEndpointContext> onChange, CompleteCallback completeCallback);
    /**
     * registers handlers for add, remove and change to the given collection path.
     * the add handler will be triggered for all elements that fit the Filter query.
    */
    void live(BiConsumer<JObject, IPathAndEndpointContext> onAdd, BiConsumer<JObject, IPathAndEndpointContext> onRemove, BiConsumer<JObject, IPathAndEndpointContext> onChange);
}
