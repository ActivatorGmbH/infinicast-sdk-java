package io.infinicast;

import io.infinicast.client.api.paths.IPathAndEndpointContext;

/**
 * Created by ocean on 05.03.2017.
 */
public interface BiConsumer<T, T1> {
    void accept(T json, T1 context);
}
