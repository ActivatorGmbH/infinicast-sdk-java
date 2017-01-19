package io.infinicast;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
    }

    public NotImplementedException(String msg) {
        super(msg);
    }

    public String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String result = "NotImplementedException with message: " + this.getMessage() + " with stacktrace: ";
        result += this.stackTraceToString(this);

        return result;
    }
}


