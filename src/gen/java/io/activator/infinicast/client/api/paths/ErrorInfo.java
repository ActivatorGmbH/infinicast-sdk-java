package io.activator.infinicast.client.api.paths;
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
