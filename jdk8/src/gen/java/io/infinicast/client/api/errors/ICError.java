package io.infinicast.client.api.errors;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
public class ICError {
    JObject _customJson;
    String _message;
    String _path;
    ICErrorType _errorType;
    public ICError(ICErrorType errorType, String message, String path, JObject customJson) {
        this.setMessage(message);
        this.setErrorType(errorType);
        this.setPath(path);
        this.setCustomJson(customJson);
    }
    public static ICError create(ICErrorType errorType, String message, String path) {
        return new ICError(errorType, message, path, null);
    }
    public static ICError create(ICErrorType errorType, String message, String path, JObject data) {
        return new ICError(errorType, message, path, data);
    }
    public static ICError fromJson(JObject job) {
        return new ICError((ICErrorType) ICErrorType.valueOf(job.getString("reason")), job.getString("error"), job.getString("path"), null);
    }
    public String toString() {
        return ((this.getErrorType().toString() + " ") + this.getMessage());
    }
    public void append(ICError icError) {
        this.setMessage(icError.getMessage());
    }
    public static ICError fromException(Exception x, String path) {
        return new ICError(ICErrorType.InternalError, InfinicastExceptionHelper.ExceptionToString(x), path, null);
    }
    public JObject toJson() {
        JObject job = new JObject();
        job.set("reason", this.getErrorType().toString());
        if (!(StringExtensions.IsNullOrEmpty(this.getMessage()))) {
            job.set("error", this.getMessage());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getPath()))) {
            job.set("path", this.getPath());
        }
        if ((this.getCustomJson() != null)) {
            job.set("customJson", this.getCustomJson());
        }
        return job;
    }
    public JObject getCustomJson() {
        return this._customJson;
    }
    public void setCustomJson(JObject value) {
        this._customJson = value;
    }
    public String getMessage() {
        return this._message;
    }
    public void setMessage(String value) {
        this._message = value;
    }
    public String getPath() {
        return this._path;
    }
    public void setPath(String value) {
        this._path = value;
    }
    public ICErrorType getErrorType() {
        return this._errorType;
    }
    public void setErrorType(ICErrorType value) {
        this._errorType = value;
    }
}
