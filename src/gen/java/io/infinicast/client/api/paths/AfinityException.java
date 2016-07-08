package io.infinicast.client.api.paths;
import io.infinicast.*;

public class AfinityException extends Exception  {
    public AfinityException(ErrorInfo info) {
        super(info.getMessage());
    }
    public String toString() {
        return super.getMessage();
    }
}
