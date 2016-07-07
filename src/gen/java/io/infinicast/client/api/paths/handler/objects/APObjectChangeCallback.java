package io.infinicast.client.api.paths.handler.objects;
import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
import io.activator.infinicast.*;

@FunctionalInterface
public interface APObjectChangeCallback {
    void accept(JObject json, IPath path, IEndpoint endpointInfo);
}
