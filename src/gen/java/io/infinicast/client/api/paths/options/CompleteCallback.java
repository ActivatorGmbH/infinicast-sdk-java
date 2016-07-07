package io.infinicast.client.api.paths.options;
import io.infinicast.client.api.paths.ErrorInfo;
import io.activator.infinicast.*;

@FunctionalInterface
public interface CompleteCallback {
    void accept(ErrorInfo error);
}
