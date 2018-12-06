package io.infinicast;

public interface ILogger {

    void logException(String where, Exception x);

    void logError(String msg);
    void logError(String msg,Exception x);

    void logInfo(String msg);

    void logDebug(String msg);

    void logWarn(String msg);
}
