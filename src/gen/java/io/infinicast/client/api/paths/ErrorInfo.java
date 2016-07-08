package io.infinicast.client.api.paths;
import io.infinicast.JObject;
import io.infinicast.StringExtensions;
import io.infinicast.*;

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
