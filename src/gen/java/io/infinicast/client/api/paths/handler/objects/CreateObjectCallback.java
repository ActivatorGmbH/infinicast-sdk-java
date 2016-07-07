package io.infinicast.client.api.paths.handler.objects;
import io.infinicast.JObject;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IAPathContext;
import io.activator.infinicast.*;

@FunctionalInterface
public interface CreateObjectCallback {
    void accept(ErrorInfo error, JObject data, IAPathContext context);
}
