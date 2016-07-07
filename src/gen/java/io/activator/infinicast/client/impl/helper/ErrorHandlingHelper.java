package io.activator.infinicast.client.impl.helper;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
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
