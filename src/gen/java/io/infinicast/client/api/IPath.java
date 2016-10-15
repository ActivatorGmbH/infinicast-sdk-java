package io.infinicast.client.api;
import io.infinicast.*;



import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;

/**
 * Everything in Infinicast is using paths. Paths are the way to share anything:
 * paths can be used to store data, send requests and send messages.
 * all data, requests, messages can be listened on and live updates can be received.
*/
public interface IPath {
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, JObject value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, double value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, int value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, String value);
    void modifyDataRemoveFromSet(String field, String value, CompletionCallback completeCallback);
     /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, JArray value);
   /**
     * comparable to rest requests.
     * Sends a Json Request to a Path. exactly one listener will be triggered with the OnRequest handler. The selected listener has to respond.
     * @param json the Message payload
     * @param answer a callback returning if an error occured, the answered json, path and endpointcontext
    */
    void sendRequest(JObject json, APRequestAnswerCallback answer);
    /**
     * Get a child of this path with the {@code name}
     * e.g. if the path was /my/path/  the result will be /my/path/name/
     * @param name Name of the path
     * UNKNOWN DOC TAG 'region'
     * @return the resulting path
    */
    IPath path(String name);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, JArray value, CompletionCallback completeCallback);
     /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the set
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSet(String field, JObject value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, double value, CompletionCallback completeCallback);
     /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, int value);
     /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, int value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, String value);
   /**
     * returns a path reference to the parent collection of this path.
     * Example: on a path /my/foo/bar/ the result would be a reference to /my/foo/
     * @return  returns a path reference to the parent collection of this path.
    */
    IPath parentPath();
    /**
     * returns an element of this path address as a string.
     * Example: on a path /my/foo/bar/ idx = 0 would return my, idx = 1 would return foo and idx = 2 would return bar
     * @param idx
     * @return
    */
    String pathAddressElement(int idx);
    void addDebugPingInfo(int pingInMs);
    void addDebugMessage(AMessageLevel level, String message);
    void addDebugMessage(AMessageLevel level, JObject message);
    IPath withAdvancedOptions(JObject obj);
    /**
     * returns a string representation of the current path e.g. /root/my/path/
     * @return the string representation of this path
    */
    String toString();
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, String value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, JArray value);
      /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, JArray value, CompletionCallback completeCallback);
   /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSet(String field, JObject value, CompletionCallback completeCallback);
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataDecValue(String field, double value);
    /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataDecValue(String field, double value, CompletionCallback completeCallback);
     /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataDecValue(String field, int value);
     /**
     * Modifies the data by decrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataDecValue(String field, int value, CompletionCallback completeCallback);
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataIncValue(String field, double value);
     /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataIncValue(String field, double value, CompletionCallback completeCallback);
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataIncValue(String field, int value);
     /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about success or failure.
    */
    void modifyDataIncValue(String field, int value, CompletionCallback completeCallback);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    void modifyDataSetValue(String field, double value);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataSetValue(String field, double value, CompletionCallback completeCallback);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    void modifyDataSetValue(String field, boolean value);
     /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataSetValue(String field, boolean value, CompletionCallback completeCallback);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    void modifyDataSetValue(String field, int value);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataSetValue(String field, int value, CompletionCallback completeCallback);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    void modifyDataSetValue(String field, String value);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataSetValue(String field, String value, CompletionCallback completeCallback);
    /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
    */
    void modifyDataSetValue(String field, JObject value);
     /**
     * Modify Path Data by setting the field to the passed value
     * @param field
     * @param value
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataSetValue(String field, JObject value, CompletionCallback completeCallback);
     /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return if the operation was successfull or not
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
    */
    void modifyDataAtomic(AtomicChange data);
    /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return if the operation was successfull or not
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataAtomic(AtomicChange data, CompletionCallback completeCallback);
    /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return the resulting json
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
    */
    void modifyDataAtomicAndGetResult(AtomicChange data);
   /**
     * Modify Path Data by providing an AtomicChange object that allows to chain operations into one atomic operation.
     * The callback function will return the resulting json
     * @param data an AtomicChange object that can chain multiple atomic changes into one big atomic change
     * @param completeCallback a callback function that indicates if the function was successfull(error=null) or failed(error contains the error in that case)
    */
    void modifyDataAtomicAndGetResult(AtomicChange data, JsonCompletionCallback completeCallback);
     /**
     * this method is deprecated and should no longer be used
     * @param callback
    */
    void onIntroduce(APObjectIntroduceCallback callback);
    /**
     * this method is deprecated and should no longer be used
     * @param callback
     * @param registrationCompleteCallback
    */
    void onIntroduce(APObjectIntroduceCallback callback, CompleteCallback registrationCompleteCallback);
     /**
     * Experimental feature:
     * registers a reminder handler that will be called on one of the listeners as soon as a reminder on this path is triggered by the system.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the reminder event
    */
    void onReminder(AReminderCallback callback);
    /**
     * Experimental feature:
     * registers a reminder handler that will be called on one of the listeners as soon as a reminder on this path is triggered by the system.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the reminder event
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    void onReminder(AReminderCallback callback, CompleteCallback registrationCompleteCallback);
     /**
     * registers a request handler that will be called on one of the listeners as soon as a request on this path is sent.
     * the responder object needs to be used to respond to the sender.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the request
    */
    void onRequest(APRequestCallback callback);
    /**
     * registers a request handler that will be called on one of the listeners as soon as a request on this path is sent.
     * the responder object needs to be used to respond to the sender.
     * the handler can be deregistered by passing null as callback
     * @param callback callback that handels the request
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    void onRequest(APRequestCallback callback, CompleteCallback registrationCompleteCallback);
    /**
     * registers a message validator on this path. A validator will be called before the message is actually sent to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
    */
    void onValidateMessage(APValidateMessageCallback callback);
    /**
     * registers a message validator on this path. A validator will be called before the message is actually sent to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    void onValidateMessage(APValidateMessageCallback callback, CompleteCallback registrationCompleteCallback);
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
    */
    void onMessage(APMessageCallback callback);
    /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    void onMessage(APMessageCallback callback, CompleteCallback registrationCompleteCallback);
   /**
     * registers a message handler on this path. Messages sent to this path will  cause the callback handler to be triggered
     * the EndpointAndPath context can be used to get the sending endpoint of th received messages
     * the handler can be deregistered by passing null as callback
     * @param callback the callback to be called when a message is sent to this path
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
     * @param listenTerminationHandler an optional parameter to get informed when the listening has been ended by the server.
    */
    void onMessage(APMessageCallback callback, CompleteCallback registrationCompleteCallback, BiConsumer<ListenTerminateReason, IAPathContext> listenTerminationHandler);
    /**
     * registers a data validator on this path. A validator will be called before the data change is applied to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
    */
    void onValidateDataChange(APValidateDataChangeCallback callback);
     /**
     * registers a data validator on this path. A validator will be called before the data change is applied to the system
     * the validtor needs to accept, change or reject the change via the responder object
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the validation occurs
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    void onValidateDataChange(APValidateDataChangeCallback callback, CompleteCallback registrationCompleteCallback);
    /**
     * registers a listener on data changes on this path
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the data changed
    */
    void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback);
    /**
     * registers a listener on data changes on this path
     * the handler can be deregistered by passing null as callback
     * @param callback callback when the data changed
     * @param registrationCompleteCallback sucessfull registration(error = null) or error
    */
    void onDataChange(TriConsumer<JObject, JObject, IPathAndEndpointContext> callback, CompleteCallback registrationCompleteCallback);
    /**
     * Experimental feature:
     * deletes a reminder by comparing the provided queryData
     * @param queryData data to identify the reminder.
    */
    void deleteReminder(JObject queryData);
    /**
     * Experimental feature:
     * deletes a reminder by comparing the provided queryData
     * @param queryData data to identify the reminder.
     * @param completeCallback called with success or error
    */
    void deleteReminder(JObject queryData, CompleteCallback completeCallback);
    /**
     * Experimental feature:
     * adds or replace a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param queryData data to identify the reminder.
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
    */
    void addOrReplaceReminder(JObject queryData, ReminderSchedulingOptions schedulingOptions, JObject json);
    /**
     * Experimental feature:
     * adds or replace a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param queryData data to identify the reminder.
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
     * @param completeCallback called with success or error
    */
    void addOrReplaceReminder(JObject queryData, ReminderSchedulingOptions schedulingOptions, JObject json, CompleteCallback completeCallback);
    /**
     * Experimental feature:
     * adds a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
    */
    void addReminder(ReminderSchedulingOptions schedulingOptions, JObject json);
     /**
     * Experimental feature:
     * adds a reminder in the cloud. exactly one of the services that is registered via OnReminder will receive the reminder
     * @param schedulingOptions scheduling options to define when the timer should be fired
     * @param json data to be added to the reminder
     * @param completeCallback called with success or error
    */
    void addReminder(ReminderSchedulingOptions schedulingOptions, JObject json, CompleteCallback completeCallback);
    /**
     * deletes the path and child paths
    */
    void delete();
    /**
     * deletes the path and child paths
     * @param completeCallback called with success or error
    */
    void delete(CompleteCallback completeCallback);
    /**
     * returns the data stored in the path
     * @param callback the returned json or an error
    */
    void getData(GetDataCallback callback);
     /**
     * returns the data stored in the path
     * @param callback the returned json or an error
     * @param options  optional parameter to add additional datacontext to the path (Note: deprecated)
    */
    void getData(GetDataCallback callback, GetDataOptions options);
   /**
     * Sets the data of this path.
     * @param json the data to be assigned
    */
    void setData(JObject json);
    /**
     * Sets the data of this path.
     * @param json the data to be assigned
     * @param completeCallback a result that indicates success or failure
    */
    void setData(JObject json, CompleteCallback completeCallback);
    /**
     * Sends a Json Message to a Path. All Endpoints currently listening on Messages on this path will receive it.
     * @param json the Message payload
    */
    void sendMessage(JObject json);
   /**
     * Sends a Json Message to a Path. All Endpoints currently listening on Messages on this path will receive it.
     * @param json the Message payload
     * @param completeCallback a callback triggered when the message  has been received by the cloud
    */
    void sendMessage(JObject json, CompleteCallback completeCallback);
   /**
     * returns the string representation of the parent path
     * Example: on a path /my/foo/bar/ the result would be a string containing '/my/foo/'
    */
    String getParentPathAddress();
    /**
     * returns the id part of this path.
     * Example: on a path /my/foo/bar/ the id would be the stirng 'bar'
    */
    String getId();
    /**
     * basically allows to use this path as a collection.
     * returns the reference to a IChildrenQuery element that can be used to modify, query, delete.. children of this path.
    */
    IChildrenQuery getChildren();
    /**
     * returns the reference to a IListenerQuery element that can be used to get informations about the listening endpoints on a given path.
    */
    IListenerQuery getListeners();
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, JArray value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, JArray value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, String value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, String value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, int value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, double value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added.
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArray(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, JObject value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, JArray value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, JArray value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, String value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, String value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, int value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, double value, CompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArray(String field, double value);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, JObject value);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, String value, JsonCompletionCallback completeCallback);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, String value);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, int value);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, boolean value, JsonCompletionCallback completeCallback);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, boolean value);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * sets the data of a given field to the value.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataSetValueAndGetResult(String field, double value);
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataIncValueAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataIncValueAndGetResult(String field, int value);
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataIncValueAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by incrementing the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataIncValueAndGetResult(String field, double value);
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataDecValueAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataDecValueAndGetResult(String field, int value);
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataDecValueAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by decrement the given field of the data in this path.
     * if the data field is not existing or not a number it will be initialized as 0.
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataDecValueAndGetResult(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, JArray value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, String value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, String value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be added only once
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToSetAndGetResult(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, JArray value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, String value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, String value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and ensures that the json will be removed from the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromSetAndGetResult(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, JArray value);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, String value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, String value);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and adds the value to the array
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataAddToArrayAndGetResult(String field, double value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, JObject value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, JObject value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, JArray value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, JArray value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, String value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, String value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, int value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, int value);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, double value, JsonCompletionCallback completeCallback);
    /**
     * Modifies the data by converting the given field in the data to an array and removes the value from the array one time
     * if the data field is not existing or not a json array it will be initialized as [].
     * a completion callback or a promise can be used to get an information about the complete data after the change or error.
    */
    void modifyDataRemoveFromArrayAndGetResult(String field, double value);
}
