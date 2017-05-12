package io.infinicast.client.impl.pathAccess;

import io.infinicast.*;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.errors.ICException;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.paths.handler.CompletionCallback;
import io.infinicast.client.api.paths.handler.JsonCompletionCallback;
import io.infinicast.client.api.paths.handler.messages.APMessageCallback;
import io.infinicast.client.api.paths.handler.messages.APValidateDataChangeCallback;
import io.infinicast.client.api.paths.handler.messages.APValidateMessageCallback;
import io.infinicast.client.api.paths.handler.objects.APObjectIntroduceCallback;
import io.infinicast.client.api.paths.handler.objects.GetDataCallback;
import io.infinicast.client.api.paths.handler.reminders.AReminderCallback;
import io.infinicast.client.api.paths.handler.requests.APRequestAnswerCallback;
import io.infinicast.client.api.paths.handler.requests.APRequestCallback;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.api.paths.taskObjects.ADataAndPathAndEndpointContext;
import io.infinicast.client.api.paths.taskObjects.ADataAndPathContext;
import io.infinicast.client.api.query.ListenTerminateReason;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.contexts.APathContext;
import io.infinicast.client.impl.helper.ErrorHandlingHelper;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.infinicast.client.utils.PathUtils;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
/**
 * Everything in Infinicast is using paths. Paths are the way to share anything:
 * paths can be used to store data, send requests and send messages.
 * all data, requests, messages can be listened on and live updates can be received.
*/
public class PathImpl implements IPath {
    ChildQueryExecutor _childQueryExecutor = null;
    ChildrenWithListenersQueryExecutor _childWithListenersQueryExecutor = null;
    PathQueryWithHandlerExecutor _pathQueryWithHandlerExecutor = null;
    Logger _logger = LoggerFactory.getLogger(PathImpl.class);
    ListenerQueryExecutor _listenerQueryExecutor = null;
    JObject _advancedOptions = null;
    String _internalPath;
    IConnector _connector;
    PathImpl _root;
    public ConnectorMessageManager messageManager;
    public PathImpl(String path) {
        this._internalPath = path;
    }
    public String getPathAddress() {
        return PathUtils.cleanup(this._internalPath);
    }
    PathImpl copy() {
        PathImpl path = new PathImpl(this._internalPath);
        path.setMessageManager(this.messageManager);
        path.setRoot(this.getRoot());
        path._advancedOptions = this._advancedOptions;
        return path;
    }
    public IPath path(String pathAddress) {
        PathImpl path = new PathImpl(this.combinePath(pathAddress));
        path.setMessageManager(this.messageManager);
        path.setRoot(this.getRoot());
        path.setConnector(this.getConnector());
        return path;
    }
    public void setConnector(IConnector connector) {
        this._connector = connector;
    }
    String combinePath(String path) {
        return PathUtils.combine(this._internalPath, path);
    }
    public void setMessageManager(ConnectorMessageManager messageManager) {
        this.messageManager = messageManager;
    }
    public IConnector getConnector() {
        if (this._root == null) {
            return this._connector;
        }
        return this.getRoot().getConnector();
    }
    public void setRoot(PathImpl root) {
        this._root = root;
        this.messageManager = root.messageManager;
    }
    public PathImpl getRoot() {
        if (this._root == null) {
            return this;
        }
        return this._root;
    }
    /**
     * returns a path reference to the parent collection of this path.
     * Example: on a path /my/foo/bar/ the result would be a reference to /my/foo/
     * @return  returns a path reference to the parent collection of this path.
    */
    public IPath parentPath() {
        return this.parentPathWithDepth(1);
    }
    public IPath parentPathWithDepth(int depth) {
        String parentPath = this._internalPath;
        for (int i = 0;
         (i < depth); (i)++) {
            parentPath = PathUtils.getParentPath(parentPath);
            if (StringExtensions.IsNullOrEmpty(parentPath)) {
                return null;
            }
        }
        PathImpl newPath = new PathImpl(parentPath);
        newPath.setMessageManager(this.messageManager);
        newPath.setRoot(this.getRoot());
        return newPath;
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, String value) {
        this.modifyDataSetValueIfEmptyAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Sets the data of this path.
     * @param json the data to be assigned
     * @return a result that indicates success or failure
    */
    public CompletableFuture<Void> setDataAsync(JObject json) {
        final CompletableFuture<Void> tsc = new CompletableFuture<Void>();
        this.setData(json, (error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tsc, error);
        });
        return tsc;
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, String value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    public CompletableFuture<JObject> modifyDataAtomicAndGetResultAsync(AtomicChange atomicChangeChange) {
        final CompletableFuture<JObject> tsc = new CompletableFuture<JObject>();
        this.modifyDataAtomicAndGetResult(atomicChangeChange, (error, data) -> {
            if (error != null) {
                tsc.completeExceptionally(new ICException(error));
            }
            else {
                tsc.complete(data);
            }
            ;
        });
        return tsc;
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, JObject value) {
        this.modifyDataSetValueIfEmptyAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    public CompletableFuture<Void> modifyDataAtomicAsync(AtomicChange atomicChangeChange) {
        final CompletableFuture<Void> tsc = new CompletableFuture<Void>();
        this.modifyDataAtomic(atomicChangeChange, (error) -> {
            if (error != null) {
                tsc.completeExceptionally(new ICException(error));
            }
            else {
                tsc.complete(null);
            }
            ;
        });
        return tsc;
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueAsync(String field, JObject value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, double value) {
        this.modifyDataSetValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueAsync(String field, String value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueAsync(String field, boolean value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, int value) {
        this.modifyDataSetValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, boolean value) {
        this.modifyDataSetValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueIfEmptyAsync(String field, JObject value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, boolean value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueIfEmptyAsync(String field, String value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, String value) {
        this.modifyDataSetValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueIfEmptyAsync(String field, boolean value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, String value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueIfEmptyAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, JObject value) {
        this.modifyDataSetValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @return a Promise indicating failure or success of the operation
    */
    public CompletableFuture<Void> modifyDataSetValueIfEmptyAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public CompletableFuture<Void> modifyDataIncValueAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().incValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, double value) {
        this.modifyDataRemoveFromArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public CompletableFuture<Void> modifyDataIncValueAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().incValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public CompletableFuture<Void> modifyDataDecValueAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().decValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, int value) {
        this.modifyDataRemoveFromArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public CompletableFuture<Void> modifyDataDecValueAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().decValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToSetAsync(String field, JObject value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, String value) {
        this.modifyDataRemoveFromArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToSetAsync(String field, JArray value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, String value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToSetAsync(String field, String value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, JArray value) {
        this.modifyDataRemoveFromArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToSetAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, JArray value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToSetAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, JObject value) {
        this.modifyDataRemoveFromArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromSetAsync(String field, JObject value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArray(String field, JObject value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromSetAsync(String field, JArray value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, double value) {
        this.modifyDataAddToArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromSetAsync(String field, String value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromSetAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, int value) {
        this.modifyDataAddToArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromSetAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToArrayAsync(String field, JObject value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, String value) {
        this.modifyDataAddToArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToArrayAsync(String field, JArray value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, String value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToArrayAsync(String field, String value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, JArray value) {
        this.modifyDataAddToArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToArrayAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, JArray value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataAddToArrayAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, JObject value) {
        this.modifyDataAddToArray(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromArrayAsync(String field, JObject value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArray(String field, JObject value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromArrayAsync(String field, JArray value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, double value) {
        this.modifyDataRemoveFromSet(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromArrayAsync(String field, String value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromArrayAsync(String field, int value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, int value) {
        this.modifyDataRemoveFromSet(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<Void> modifyDataRemoveFromArrayAsync(String field, double value) {
        return this.modifyDataAtomicAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueAndGetResultAsync(String field, JObject value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, String value) {
        this.modifyDataRemoveFromSet(field, value, (CompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueAndGetResultAsync(String field, String value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, String value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueAndGetResultAsync(String field, boolean value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, JArray value) {
        this.modifyDataRemoveFromSet(field, value, (CompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, JArray value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, JObject value) {
        this.modifyDataRemoveFromSet(field, value, (CompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueIfEmptyAndGetResultAsync(String field, JObject value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSet(String field, JObject value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueIfEmptyAndGetResultAsync(String field, String value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, double value) {
        this.modifyDataAddToSet(field, value, (CompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueIfEmptyAndGetResultAsync(String field, boolean value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueIfEmptyAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, int value) {
        this.modifyDataAddToSet(field, value, (CompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataSetValueIfEmptyAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().setValueIfEmpty(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataIncValueAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().incValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, String value) {
        this.modifyDataAddToSet(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataIncValueAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().incValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, String value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataDecValueAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().decValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, JArray value) {
        this.modifyDataAddToSet(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataDecValueAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().decValue(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, JArray value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToSetAndGetResultAsync(String field, JObject value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, JObject value) {
        this.modifyDataAddToSet(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToSetAndGetResultAsync(String field, JArray value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSet(String field, JObject value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToSetAndGetResultAsync(String field, String value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataDecValue(String field, double value) {
        this.modifyDataDecValue(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToSetAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataDecValue(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().decValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToSetAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToSet(field, value));
    }
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataDecValue(String field, int value) {
        this.modifyDataDecValue(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromSetAndGetResultAsync(String field, JObject value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataDecValue(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().decValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromSetAndGetResultAsync(String field, JArray value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataIncValue(String field, double value) {
        this.modifyDataIncValue(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromSetAndGetResultAsync(String field, String value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataIncValue(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().incValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromSetAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataIncValue(String field, int value) {
        this.modifyDataIncValue(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromSetAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromSet(field, value));
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    public void modifyDataIncValue(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().incValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToArrayAndGetResultAsync(String field, JObject value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
    */
    public void modifyDataSetValueIfEmpty(String field, double value) {
        this.modifyDataSetValueIfEmpty(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToArrayAndGetResultAsync(String field, JArray value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValueIfEmpty(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToArrayAndGetResultAsync(String field, String value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
    */
    public void modifyDataSetValueIfEmpty(String field, int value) {
        this.modifyDataSetValueIfEmpty(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToArrayAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValueIfEmpty(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataAddToArrayAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().addToArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
    */
    public void modifyDataSetValueIfEmpty(String field, boolean value) {
        this.modifyDataSetValueIfEmpty(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromArrayAndGetResultAsync(String field, JObject value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValueIfEmpty(String field, boolean value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromArrayAndGetResultAsync(String field, JArray value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
    */
    public void modifyDataSetValueIfEmpty(String field, String value) {
        this.modifyDataSetValueIfEmpty(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromArrayAndGetResultAsync(String field, String value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValueIfEmpty(String field, String value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromArrayAndGetResultAsync(String field, int value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
    */
    public void modifyDataSetValueIfEmpty(String field, JObject value) {
        this.modifyDataSetValueIfEmpty(field, value, (CompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public CompletableFuture<JObject> modifyDataRemoveFromArrayAndGetResultAsync(String field, double value) {
        return this.modifyDataAtomicAndGetResultAsync(new AtomicChange().removeFromArray(field, value));
    }
    /**
     * Modify Path Data by setting the field to the passed value if the field is empty
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValueIfEmpty(String field, JObject value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    JObject applyAdvancedOptions(JObject data) {
        JObject realData = data;
        if (this._advancedOptions != null) {
            if (realData == null) {
                realData = new JObject();
            }
            if (this._advancedOptions.get("accessId") != null) {
                data.set("accessId", this._advancedOptions.getString("accessId"));
            }
        }
        return realData;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    public void modifyDataSetValue(String field, double value) {
        this.modifyDataSetValue(field, value, (CompletionCallback) null);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValue(String field, double value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * deletes the path. Does not affect child paths!
     * @return promise containg success or error
    */
    public CompletableFuture<Void> deleteDataAsync() {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.deleteData((error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tcs, error);
        });
        return tcs;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    public void modifyDataSetValue(String field, int value) {
        this.modifyDataSetValue(field, value, (CompletionCallback) null);
    }
    /**
     * deletes the path and all listeners on the path as well as the roles directly added to this path
     * @return promise containg success or error
    */
    public CompletableFuture<Void> deleteDataAndListenersAsync() {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.deleteDataAndListeners((error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tcs, error);
        });
        return tcs;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValue(String field, int value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * deletes all listeners on the path
     * @return promise containg success or error
    */
    public CompletableFuture<Void> deleteListenersAsync() {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.deleteListeners((error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tcs, error);
        });
        return tcs;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    public void modifyDataSetValue(String field, boolean value) {
        this.modifyDataSetValue(field, value, (CompletionCallback) null);
    }
    /**
     * Experimental feature:
     * adds a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
     * @return success or error
    */
    public CompletableFuture<Void> addReminderAsync(ReminderSchedulingOptions schedulingOptions, JObject json) {
        return this.addOrReplaceReminderAsync(null, schedulingOptions, json);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValue(String field, boolean value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValue(field, value), completeCallback);
    }
    public CompletableFuture<Void> addOrReplaceReminderAsync(JObject queryJson, ReminderSchedulingOptions schedulingOptions, JObject json) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.addReminder(schedulingOptions, json, (error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tcs, error);
        });
        return tcs;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    public void modifyDataSetValue(String field, String value) {
        this.modifyDataSetValue(field, value, (CompletionCallback) null);
    }
    public CompletableFuture<Void> deleteReminderAsync(JObject queryJson) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.deleteReminder(queryJson, (error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tcs, error);
        });
        return tcs;
    }
    public void sendRequest(JObject data, final APRequestAnswerCallback answer) {
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.Request, this, data, (json, err, context) -> {
            if (!(this.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                err.setCustomJson(json);
                answer.accept(error, null, null);
                ;
            }))) {
                answer.accept(null, json, context);
                ;
            }
            ;
        });
    }
    public CompletableFuture<ADataAndPathAndEndpointContext> sendRequestAsync(JObject data) {
        final CompletableFuture<ADataAndPathAndEndpointContext> tcs = new CompletableFuture<ADataAndPathAndEndpointContext>();
        this.sendRequest(data, (error, json, context) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                ADataAndPathAndEndpointContext res = new ADataAndPathAndEndpointContext();
                res.setContext(context);
                res.setData(json);
                tcs.complete(res);
            }
            ;
        });
        return tcs;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValue(String field, String value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Sends a Json Message to a Path. All Endpoints currently listening on Messages on this path will receive it.
     * @param json the Message payload
     * @return a promise that completes when the message has been received by the cloud
    */
    public CompletableFuture<Void> sendMessageAsync(JObject json) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.sendMessage(json, (error) -> {
            PathImpl.handleCompleteHandlerAsyncVoid(tcs, error);
        });
        return tcs;
    }
    public void addDebugPingInfo(int pingInMs) {
        this.messageManager.sendDebugPingInfo(this, pingInMs);
    }
    public void addDebugMessage(AMessageLevel level, String message) {
        JObject json = new JObject();
        json.set("text", message);
        this.addDebugMessage(level, json);
    }
    public void addDebugMessage(AMessageLevel level, JObject message) {
        JObject data = new JObject();
        data.set("level", level.getValue());
        data.set("info", message);
        this.messageManager.sendDebugMessage(this, level.getValue(), data);
    }
    public IPath withAdvancedOptions(JObject obj) {
        PathImpl path = this.copy();
        path._advancedOptions = obj;
        return path;
    }
    static void handleCompleteHandlerAsyncVoid(CompletableFuture<Void> tcs, ICError icError) {
        if (icError != null) {
            tcs.completeExceptionally(new ICException(icError));
        }
        else {
            tcs.complete(null);
        }
    }
    static HashMap<String, Integer> getRoleCountDictionary(JObject json) {
        HashMap<String, Integer> roleCount = new HashMap<String, Integer>();
        JArray roleCountArray = json.getJArray("roleCount");
        if (roleCountArray != null) {
            for (JToken roleOb : roleCountArray) {
                String role = roleOb.getString("role");
                String handlerType = roleOb.getString("handlerType");
                int count = roleOb.getInt("count");
                if (StringExtensions.areEqual(handlerType, "Message")) {
                    roleCount.put(role, count);
                }
            }
        }
        return roleCount;
    }
    /**
     * returns a map of key value pairs via the passed paramString.
     * Example: on a path /my/foo/bar/ ExtractPathParams with a string /my/$var1/$var2/ would return a map:{var1:foo, var2: bar}
     * @param paramString a param string path e.g. /my/$var2/$anyVariableName/
     * @return
    */
    public HashMap<String, String> extractPathParams(String paramString) {
        return PathUtils.parsePath(paramString, this.toString());
    }
    /**
     * returns an element of this path address as a string.
     * Example: on a path /my/foo/bar/ idx = 0 would return my, idx = 1 would return foo and idx = 2 would return bar
     * @param idx
     * @return
    */
    public String pathAddressElement(int idx) {
        return PathUtils.getPathAddressElement(this.getPathAddress(), idx);
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    public void modifyDataSetValue(String field, JObject value) {
        this.modifyDataSetValue(field, value, (CompletionCallback) null);
    }
    /**
     * registers a listener on data changes on this path
     * @param callback callback when the data changed
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onDataChangeAsync(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onDataChange(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataSetValue(String field, JObject value, CompletionCallback completeCallback) {
        this.modifyDataAtomic(new AtomicChange().setValue(field, value), completeCallback);
    }
    /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return if the operation was successfull or not
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
    */
    public void modifyDataAtomic(AtomicChange data) {
        this.modifyDataAtomic(data, (CompletionCallback) null);
    }
    /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return if the operation was successfull or not
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataAtomic(AtomicChange data, final CompletionCallback completeCallback) {
        JObject json = new JObject();
        json.set("changes", data.toJson());
        if (data.hasNamedQueries()) {
            json.set("named", data.getNamedQueryJson());
        }
        json.set("returnData", false);
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.UpdateData, this, json, (resultJson, err, context) -> {
            if (!(this.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                if (completeCallback != null) {
                    completeCallback.accept(error);
                    ;
                }
                else {
                    this.getConnector().unhandeledErrorInfo(this, error);
                }
                ;
            }))) {
                if (completeCallback != null) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        });
    }
    /**
     * registers a data validator on this path. A validator will be called before the data change is applied to the system
     * the validator needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the data changed
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onValidateDataChangeAsync(APValidateDataChangeCallback callback) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onValidateDataChange(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return the resulting json
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
    */
    public void modifyDataAtomicAndGetResult(AtomicChange data) {
        this.modifyDataAtomicAndGetResult(data, (JsonCompletionCallback) null);
    }
    /**
     * registers a message validator on this path. A validator will be called before the message is actually sent to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onValidateMessageAsync(APValidateMessageCallback callback) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onValidateMessage(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return the resulting json
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    public void modifyDataAtomicAndGetResult(AtomicChange data, final JsonCompletionCallback completeCallback) {
        JObject json = new JObject();
        json.set("changes", data.toJson());
        if (data.hasNamedQueries()) {
            json.set("named", data.getNamedQueryJson());
        }
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.UpdateData, this, json, (resultJson, err, context) -> {
            if (!(this.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                if (completeCallback != null) {
                    completeCallback.accept(error, null);
                    ;
                }
                else {
                    this.getConnector().unhandeledErrorInfo(this, error);
                }
                ;
            }))) {
                if (completeCallback != null) {
                    completeCallback.accept(null, resultJson.getJObject("data"));
                    ;
                }
            }
            ;
        });
    }
    /**
     * registers a request handler that will be called on one of the listeners as soon as a request on this path is sent.
     * the responder object needs to be used to respond to the sender.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the request
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onRequestAsync(APRequestCallback callback) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onRequest(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * Sets the data of this path.
     * @param json the data to be assigned
    */
    public void setData(JObject json) {
        this.setData(json, (CompleteCallback) null);
    }
    /**
     * Experimental feature:
     * registers a reminder handler that will be called on one of the listeners as soon as a reminder on this path is triggered by the system.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the reminder event
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onReminderAsync(AReminderCallback callback) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onReminder(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    /**
     * Sets the data of this path.
     * @param json the data to be assigned
     * @param completeCallback a result that indicates success or failure
    */
    public void setData(JObject json, final CompleteCallback completeCallback) {
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.SetObjectData, this, json, (resultJson, error, context) -> {
            if (!(this.checkIfHasErrorsAndCallHandlersNew(error, completeCallback))) {
                if (completeCallback != null) {
                    completeCallback.accept(null);
                    ;
                }
            }
            ;
        });
    }
    /**
     * this method is deprecated and should no longer be used
     * @param callback
    */
    public CompletableFuture<Void> onIntroduceAsync(APObjectIntroduceCallback callback) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onIntroduce(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        });
        return tcs;
    }
    public boolean checkIfHasErrorsAndCallHandlersNew(ICError error, CompleteCallback completeCallback) {
        return ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersNew(this.getConnector(), error, completeCallback, this);
    }
    public void checkIfHasErrorsAndCallHandlersFull(ICError error, CompleteCallback completeCallback) {
        ErrorHandlingHelper.checkIfHasErrorsAndCallHandlersFull(this.getConnector(), error, completeCallback, this);
    }
    IAPathContext getPathAndEndpointContext(IPathAndEndpointContext ctx) {
        APathContext context = new APathContext();
        context.setPath(ctx.getPath());
        return context;
    }
    IAPathContext getPathContext(IPath path) {
        APathContext context = new APathContext();
        context.setPath(path);
        return context;
    }
    /**
     * returns a string representation of the current path e.g. /root/my/path/
     * @return the string representation of this path
    */
    public String toString() {
        return this.getPathAddress();
    }
    PathQueryWithHandlerExecutor getExecutorForPathHandler() {
        if (this._pathQueryWithHandlerExecutor == null) {
            this._pathQueryWithHandlerExecutor = new PathQueryWithHandlerExecutor(this.getRoot().getConnector(), this, this.messageManager);
        }
        return this._pathQueryWithHandlerExecutor;
    }
    /**
     * basically allows to use this path as a data collection.
     * returns the reference to a IChildrenQuery element that can be used to modify, query, delete.. children of this path.
    */
    public IChildrenQuery getChildren() {
        if (this._childQueryExecutor == null) {
            this._childQueryExecutor = new ChildQueryExecutor(this.getRoot().getConnector(), this, this.messageManager);
        }
        return new ChildrenQuery(this, this._childQueryExecutor);
    }
    /**
     * basically allows to use this path as a collection based on listeners.
     * All fitting paths that currently have listeners will be added.
     * returns the reference to a IPathByListenersQuery element that can be used to filter and get children of this path.
    */
    public IPathByListenersQuery getPathByListeners() {
        if (this._childWithListenersQueryExecutor == null) {
            this._childWithListenersQueryExecutor = new ChildrenWithListenersQueryExecutor(this.getRoot().getConnector(), this, this.messageManager);
        }
        return new PathByListenersQuery(this, this._childWithListenersQueryExecutor);
    }
    /**
     * returns the reference to a IListenerQuery element that can be used to get informations about the listening endpoints on a given path.
    */
    public IListenerQuery getListeners() {
        if (this._listenerQueryExecutor == null) {
            this._listenerQueryExecutor = new ListenerQueryExecutor(this.getRoot().getConnector(), this, this.messageManager);
        }
        return new ListenerQuery(this, this._listenerQueryExecutor);
    }
    /**
     * returns the string representation of the parent path
     * Example: on a path /my/foo/bar/ the result would be a string containing '/my/foo/'
    */
    public String getParentPathAddress() {
        return PathUtils.getObjectListPath(this.getPathAddress());
    }
    /**
     * returns the id part of this path.
     * Example: on a path /my/foo/bar/ the id would be the stirng 'bar'
    */
    public String getId() {
        return PathUtils.getLastPathPart(this.getPathAddress());
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, boolean value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, boolean value) {
        this.modifyDataSetValueIfEmptyAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, int value) {
        this.modifyDataSetValueIfEmptyAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().setValueIfEmpty(field, value), completeCallback);
    }
    /**
     * sets the data of a given field to the value if the field is currently empty.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataSetValueIfEmptyAndGetResult(String field, double value) {
        this.modifyDataSetValueIfEmptyAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataIncValueAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().incValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataIncValueAndGetResult(String field, int value) {
        this.modifyDataIncValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataIncValueAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().incValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataIncValueAndGetResult(String field, double value) {
        this.modifyDataIncValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataDecValueAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().decValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataDecValueAndGetResult(String field, int value) {
        this.modifyDataDecValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataDecValueAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().decValue(field, value), completeCallback);
    }
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataDecValueAndGetResult(String field, double value) {
        this.modifyDataDecValueAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, JObject value) {
        this.modifyDataAddToSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, JArray value) {
        this.modifyDataAddToSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, String value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, String value) {
        this.modifyDataAddToSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, int value) {
        this.modifyDataAddToSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToSetAndGetResult(String field, double value) {
        this.modifyDataAddToSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, JObject value) {
        this.modifyDataRemoveFromSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, JArray value) {
        this.modifyDataRemoveFromSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, String value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, String value) {
        this.modifyDataRemoveFromSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, int value) {
        this.modifyDataRemoveFromSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromSet(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromSetAndGetResult(String field, double value) {
        this.modifyDataRemoveFromSetAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, JObject value) {
        this.modifyDataAddToArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, JArray value) {
        this.modifyDataAddToArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, String value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, String value) {
        this.modifyDataAddToArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, int value) {
        this.modifyDataAddToArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().addToArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataAddToArrayAndGetResult(String field, double value) {
        this.modifyDataAddToArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, JObject value) {
        this.modifyDataRemoveFromArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, JArray value) {
        this.modifyDataRemoveFromArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, String value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, String value) {
        this.modifyDataRemoveFromArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, int value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, int value) {
        this.modifyDataRemoveFromArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, double value, JsonCompletionCallback completeCallback) {
        this.modifyDataAtomicAndGetResult(new AtomicChange().removeFromArray(field, value), completeCallback);
    }
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    public void modifyDataRemoveFromArrayAndGetResult(String field, double value) {
        this.modifyDataRemoveFromArrayAndGetResult(field, value, (JsonCompletionCallback) null);
    }
    /**
     * returns the data stored in the path
     * @param callback the returned json or an error
     * @param options  optional parameter to add additional datacontext to the path (Note: deprecated)
    */
    public void getData(final GetDataCallback callback, GetDataOptions options) {
        JObject data = null;
        if (options != null) {
            data = new JObject();
        }
        data = this.applyAdvancedOptions(data);
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetObjectData, this, data, (json, err, context) -> {
            if (!(this.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                callback.accept(error, null, null);
                ;
            }))) {
                callback.accept(null, json, this.getPathAndEndpointContext(context));
                ;
            }
            ;
        });
    }
    /**
     * returns the data stored in the path
     * @param callback the returned json or an error
    */
    public void getData(final GetDataCallback callback) {
        this.getData(callback, (GetDataOptions) null);
    }
    /**
     * returns the data stored in the path
     * @param options  optional parameter to add additional datacontext to the path (Note: deprecated)
     * @return the returned json or an error
    */
    public CompletableFuture<ADataAndPathContext> getDataAsync(GetDataOptions options) {
        final CompletableFuture<ADataAndPathContext> tsc = new CompletableFuture<ADataAndPathContext>();
        this.getData((error, json, context) -> {
            if (error != null) {
                tsc.completeExceptionally(new ICException(error));
            }
            else {
                ADataAndPathContext resultContext = new ADataAndPathContext();
                resultContext.data = json;
                resultContext.context = context;
                tsc.complete(resultContext);
            }
            ;
        }
        , options);
        return tsc;
    }
    /**
     * returns the data stored in the path
     * @return the returned json or an error
    */
    public CompletableFuture<ADataAndPathContext> getDataAsync() {
        return this.getDataAsync((GetDataOptions) null);
    }
    /**
     * deletes the path. Does not affect child paths!
     * @param completeCallback called with success or error
    */
    public void deleteData(final CompleteCallback completeCallback) {
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.DeleteFromCollection, this, new JObject(), (json, err, context) -> {
            this.checkIfHasErrorsAndCallHandlersFull(err, completeCallback);
        });
    }
    /**
     * deletes the path. Does not affect child paths!
    */
    public void deleteData() {
        this.deleteData((CompleteCallback) null);
    }
    /**
     * deletes the path and all listeners on the path as well as the roles directly added to this path
     * @param completeCallback called with success or error
    */
    public void deleteDataAndListeners(final CompleteCallback completeCallback) {
        JObject options = new JObject();
        options.set("listeners", true);
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.DeleteFromCollection, this, options, (json, err, context) -> {
            this.checkIfHasErrorsAndCallHandlersFull(err, completeCallback);
        });
    }
    /**
     * deletes the path and all listeners on the path as well as the roles directly added to this path
    */
    public void deleteDataAndListeners() {
        this.deleteDataAndListeners((CompleteCallback) null);
    }
    /**
     * deletes all listeners on the path
     * @param completeCallback called with success or error
    */
    public void deleteListeners(final CompleteCallback completeCallback) {
        JObject options = new JObject();
        options.set("listeners", true);
        options.set("data", false);
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.DeleteFromCollection, this, options, (json, err, context) -> {
            this.checkIfHasErrorsAndCallHandlersFull(err, completeCallback);
        });
    }
    /**
     * deletes all listeners on the path
    */
    public void deleteListeners() {
        this.deleteListeners((CompleteCallback) null);
    }
    /**
     * Experimental feature:
     * adds a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
     * @param completeCallback called with success or error
    */
    public void addReminder(ReminderSchedulingOptions schedulingOptions, JObject json, CompleteCallback completeCallback) {
        this.addOrReplaceReminder(null, schedulingOptions, json, completeCallback);
    }
    /**
     * Experimental feature:
     * adds a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
    */
    public void addReminder(ReminderSchedulingOptions schedulingOptions, JObject json) {
        this.addReminder(schedulingOptions, json, (CompleteCallback) null);
    }
    public void addOrReplaceReminder(JObject queryJson, ReminderSchedulingOptions schedulingOptions, JObject json, final CompleteCallback completeCallback) {
        JObject data = new JObject();
        if (queryJson != null) {
            data.set("query", queryJson);
        }
        data.set("data", json);
        data.set("scheduled", schedulingOptions.toJson());
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.AddReminder, this, data, (resultJson, err, context) -> {
            this.checkIfHasErrorsAndCallHandlersFull(err, completeCallback);
        });
    }
    public void addOrReplaceReminder(JObject queryJson, ReminderSchedulingOptions schedulingOptions, JObject json) {
        this.addOrReplaceReminder(queryJson, schedulingOptions, json, (CompleteCallback) null);
    }
    public void deleteReminder(JObject queryJson, final CompleteCallback completeCallback) {
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.DeleteReminder, this, queryJson, (resultJson, error, context) -> {
            this.checkIfHasErrorsAndCallHandlersFull(error, completeCallback);
        });
    }
    public void deleteReminder(JObject queryJson) {
        this.deleteReminder(queryJson, (CompleteCallback) null);
    }
    /**
     * Sends a Json Message to a Path. All Endpoints currently listening on Messages on this path will receive it.
     * @param json the Message payload
     * @param completeCallback a callback triggered when the message  has been received by the cloud
    */
    public void sendMessage(JObject json, final CompleteCallback completeCallback) {
        this.messageManager.sendMessageWithResponse(Connector2EpsMessageType.Message, this, json, (resultJson, err, context) -> {
            this.checkIfHasErrorsAndCallHandlersFull(err, completeCallback);
        });
    }
    /**
     * Sends a Json Message to a Path. All Endpoints currently listening on Messages on this path will receive it.
     * @param json the Message payload
    */
    public void sendMessage(JObject json) {
        this.sendMessage(json, (CompleteCallback) null);
    }
    /**
     * registers a listener on data changes on this path
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the data changed
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    public void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback, final CompleteCallback registrationCompleteCallback) {
        this.getExecutorForPathHandler().onDataChange(callback, null, (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        });
    }
    /**
     * registers a listener on data changes on this path
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the data changed
    */
    public void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback) {
        this.onDataChange(callback, (CompleteCallback) null);
    }
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
     * @param listenTerminationHandler an optional parameter to get informed when the listening has been ended by the server.
    */
    public void onMessage(APMessageCallback callback, final CompleteCallback registrationCompleteCallback, BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler) {
        this.getExecutorForPathHandler().onMessage(callback, null, (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        }
        , listenTerminationHandler);
    }
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    public void onMessage(APMessageCallback callback, final CompleteCallback registrationCompleteCallback) {
        this.onMessage(callback, registrationCompleteCallback, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
    */
    public void onMessage(APMessageCallback callback) {
        this.onMessage(callback, (CompleteCallback) null, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
     * @param listenTerminationHandler an optional parameter to get informed when the listening has been ended by the server.
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onMessageAsync(APMessageCallback callback, BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler) {
        final CompletableFuture<Void> tcs = new CompletableFuture<Void>();
        this.onMessage(callback, (error) -> {
            if (error != null) {
                tcs.completeExceptionally(new ICException(error));
            }
            else {
                tcs.complete(null);
            }
            ;
        }
        , listenTerminationHandler);
        return tcs;
    }
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
     * @return a promise indicating success or error
    */
    public CompletableFuture<Void> onMessageAsync(APMessageCallback callback) {
        return this.onMessageAsync(callback, (BiConsumer<ListenTerminateReason, IAPathContext>) null);
    }
    /**
     * registers a data validator on this path. A validator will be called before the data change is applied to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    public void onValidateDataChange(APValidateDataChangeCallback callback, final CompleteCallback registrationCompleteCallback) {
        this.getExecutorForPathHandler().onValidateDataChange(callback, null, (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        });
    }
    /**
     * registers a data validator on this path. A validator will be called before the data change is applied to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
    */
    public void onValidateDataChange(APValidateDataChangeCallback callback) {
        this.onValidateDataChange(callback, (CompleteCallback) null);
    }
    /**
     * registers a message validator on this path. A validator will be called before the message is actually sent to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    public void onValidateMessage(APValidateMessageCallback callback, final CompleteCallback registrationCompleteCallback) {
        this.getExecutorForPathHandler().onValidateMessage(callback, null, (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        });
    }
    /**
     * registers a message validator on this path. A validator will be called before the message is actually sent to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
    */
    public void onValidateMessage(APValidateMessageCallback callback) {
        this.onValidateMessage(callback, (CompleteCallback) null);
    }
    /**
     * registers a request handler that will be called on one of the listeners as soon as a request on this path is sent.
     * the responder object needs to be used to respond to the sender.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the request
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    public void onRequest(final APRequestCallback callback, final CompleteCallback registrationCompleteCallback) {
        this.getExecutorForPathHandler().onRequest((json, responder, context) -> {
            callback.accept(json, responder, context);
            ;
        }
        , null, (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        });
    }
    /**
     * registers a request handler that will be called on one of the listeners as soon as a request on this path is sent.
     * the responder object needs to be used to respond to the sender.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the request
    */
    public void onRequest(final APRequestCallback callback) {
        this.onRequest(callback, (CompleteCallback) null);
    }
    /**
     * Experimental feature:
     * registers a reminder handler that will be called on one of the listeners as soon as a reminder on this path is triggered by the system.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the reminder event
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    public void onReminder(AReminderCallback callback, final CompleteCallback registrationCompleteCallback) {
        this.getExecutorForPathHandler().onReminder(callback, null, (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        });
    }
    /**
     * Experimental feature:
     * registers a reminder handler that will be called on one of the listeners as soon as a reminder on this path is triggered by the system.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the reminder event
    */
    public void onReminder(AReminderCallback callback) {
        this.onReminder(callback, (CompleteCallback) null);
    }
    /**
     * this method is deprecated and should no longer be used
     * @param callback
     * @param registrationCompleteCallback
    */
    public void onIntroduce(final APObjectIntroduceCallback callback, final CompleteCallback registrationCompleteCallback) {
        this.getExecutorForPathHandler().onIntroduce((data, context) -> {
            callback.accept(data, context);
            ;
        }
        , (error) -> {
            if (registrationCompleteCallback != null) {
                registrationCompleteCallback.accept(error);
                ;
            }
            else if (error != null) {
                this.getExecutorForPathHandler().unhandeledError(error);
            }
            ;
        });
    }
    /**
     * this method is deprecated and should no longer be used
     * @param callback
    */
    public void onIntroduce(final APObjectIntroduceCallback callback) {
        this.onIntroduce(callback, (CompleteCallback) null);
    }
}
