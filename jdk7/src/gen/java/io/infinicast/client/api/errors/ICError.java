package io.infinicast.client.api.errors;

import io.infinicast.InfinicastExceptionHelper;
import io.infinicast.JObject;
import io.infinicast.StringExtensions;
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
