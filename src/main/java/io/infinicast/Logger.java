package io.infinicast;

// TODO: use proper logger
public class Logger {
    public void exception(String where, Exception x) {
        System.err.print("Exception thrown");
        System.err.println(x);
    }

    public void error(String msg) {
        System.err.println(msg);
    }

    public void warn(String msg) {
        System.out.println(msg);
    }

    public void debug(String msg) {
        System.out.println(msg);
    }

    public void info(String msg) {
        System.out.println(msg);
    }

    public boolean getIsDebugEnabled() {
        return LoggerSettings.CurrentLogLevel == LogLevel.Debug;
    }

    public void error(String s, Exception ex) {
        System.err.println(s + " " + ex.toString());
    }
}


