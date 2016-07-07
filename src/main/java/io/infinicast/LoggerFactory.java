package io.infinicast;

public class LoggerFactory {
    public static Logger getLogger(String name) {
        return new Logger();
    }
    public static Logger getLogger(Class<?> klass) {
        return new Logger();
    }
}


