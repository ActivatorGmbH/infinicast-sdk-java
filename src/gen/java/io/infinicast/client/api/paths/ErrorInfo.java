package io.infinicast.client.api.paths;
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
public class ErrorInfo {
    String _message;
    /**
     * DO NOT USE directly. Must be public only for the transpiled C++ code.
     * @param message
     * @param path
    */
    public ErrorInfo(String message, String path) {
        this.setMessage(message);
        if (!(StringExtensions.IsNullOrEmpty(path))) {
            this.setMessage(((this.getMessage() + " path: ") + path));
        }
    }
    public static ErrorInfo fromMessage(String message, String path) {
        return new ErrorInfo(message, path);
    }
    public static ErrorInfo fromJson(JObject errorJson, String path) {
        return new ErrorInfo(errorJson.getString("msg"), path);
    }
    public String toString() {
        return this.getMessage();
    }
    public void append(ErrorInfo error) {
        this.setMessage(error.getMessage());
    }
    public static ErrorInfo fromException(Exception x, String path) {
        return ErrorInfo.fromMessage(x.getMessage(), path);
    }
    public String getMessage() {
        return this._message;
    }
    public void setMessage(String value) {
        this._message = value;
    }
}
