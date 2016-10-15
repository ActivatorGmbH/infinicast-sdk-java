package io.infinicast.client.impl.helper;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;

import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
