package io.infinicast.client.impl.helper;

import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.options.CompleteCallback;
import io.infinicast.client.impl.IConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlingHelper {
    private static Logger logger = LoggerFactory.getLogger(ErrorHandlingHelper.class);
    public static void checkIfHasErrorsAndCallHandlersFull(IConnector connector, ICError error, CompleteCallback completeCallback, IPath path) {
        if (error != null) {
            if (completeCallback != null) {
                completeCallback.accept(error);
                ;
            }
            else {
                connector.unhandeledErrorInfo(path, error);
            }
            return ;
        }
        if (completeCallback != null) {
            completeCallback.accept(null);
            ;
        }
    }
    public static boolean checkIfHasErrorsAndCallHandlersNew(IConnector connector, ICError error, CompleteCallback completeCallback, IPath path) {
        if (error != null) {
            if (completeCallback != null) {
                completeCallback.accept(error);
                ;
            }
            else {
                connector.unhandeledErrorInfo(path, error);
            }
            return true;
        }
        return false;
    }
}
