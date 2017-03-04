package io.infinicast;

import io.infinicast.client.impl.contexts.APListeningStartedContext;

/**
 * Created by ocean on 05.03.2017.
 */
public interface Consumer<T> {
    void accept(T context);
}
