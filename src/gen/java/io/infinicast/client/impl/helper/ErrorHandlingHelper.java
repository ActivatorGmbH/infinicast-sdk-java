package io.infinicast.client.impl.helper;
import io.infinicast.JObject;
import io.infinicast.Logger;
import io.infinicast.LoggerFactory;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.impl.IConnector;
import io.infinicast.*;

public class ErrorHandlingHelper {
    static Logger _logger = LoggerFactory.getLogger(ErrorHandlingHelper.class);
    public static void checkIfHasErrorsAndCallHandlersFull(IConnector connector, JObject json, CompleteCallback completeCallback, IPath path) {
        if ((json != null)) {
            JObject errorJson = json.getJObject("error");
            if (((json != null) && (errorJson != null))) {
                if ((completeCallback != null)) {
                    String pathAddress = "";
                    if ((path != null)) {
                        path.toString();
                    }
                    completeCallback.accept(ErrorInfo.fromJson(errorJson, pathAddress));
                    ;
                }
                else {
                    connector.unhandeledError(path, errorJson);
                }
                return ;
            }
        }
        else {
            ErrorHandlingHelper._logger.warn("Note: no resulting json set.");
        }
        if ((completeCallback != null)) {
            completeCallback.accept(null);
            ;
        }
    }
    public static boolean checkIfHasErrorsAndCallHandlersNew(IConnector connector, JObject json, CompleteCallback completeCallback, IPath path) {
        if ((json != null)) {
            JObject errorJson = json.getJObject("error");
            if (((json != null) && (errorJson != null))) {
                if ((completeCallback != null)) {
                    String pathAddress = "";
                    if ((path != null)) {
                        path.toString();
                    }
                    completeCallback.accept(ErrorInfo.fromJson(errorJson, pathAddress));
                    ;
                }
                else {
                    connector.unhandeledError(path, errorJson);
                }
                return true;
            }
        }
        else {
            ErrorHandlingHelper._logger.warn("Note: no resulting json set.");
        }
        return false;
    }
}
