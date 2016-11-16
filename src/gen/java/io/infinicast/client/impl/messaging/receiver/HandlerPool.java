package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.Action;
import io.infinicast.ThreadPool;

import java.util.function.Consumer;
public class HandlerPool {
    public void queueHandlerCall(final Action call) {
        HandlerPool self = this;
        ThreadPool.QueueUserWorkItem(new Consumer<Object>() {
            public void accept(Object state) {
                call.accept();
                ;
            }
        }
        );
    }
}
