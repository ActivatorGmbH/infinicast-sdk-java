package io.infinicast;

public class Logger {

    public static ILogger _logger;
    public static void SetLogger(ILogger logger){
        _logger = logger;
    }
    public void exception(String where, Exception x) {
        if(_logger!=null){
            _logger.logException(where,x);
        }else{
            System.err.print("Exception thrown");
            System.err.println(x);
        }
    }

    public void error(String msg) {

        if(_logger!=null){
            _logger.logError(msg);
        }else {
            System.err.println(msg);
        }
    }

    public void warn(String msg) {
        if(_logger!=null){
            _logger.logWarn(msg);
        }else {
            System.out.println(msg);
        }
    }

    public void debug(String msg) {
        if(_logger!=null){
            _logger.logDebug(msg);
        }else {
            System.out.println(msg);
        }
    }

    public void info(String msg) {
        if(_logger!=null){
            _logger.logInfo(msg);
        }else {
            System.out.println(msg);
        }
    }

    public boolean getIsDebugEnabled() {
        return LoggerSettings.CurrentLogLevel == LogLevel.Debug;
    }

    public void error(String s, Exception ex) {
        if(_logger!=null){
            _logger.logError(s,ex);
        }else {
            System.err.println(s + " " + ex.toString());
        }
    }
}


