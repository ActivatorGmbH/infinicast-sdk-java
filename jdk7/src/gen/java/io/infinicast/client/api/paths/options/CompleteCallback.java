package io.infinicast.client.api.paths.options;

import io.infinicast.FunctionalInterface;
import io.infinicast.client.api.errors.ICError;
@FunctionalInterface
public interface CompleteCallback {
    void accept(ICError icError);
}
