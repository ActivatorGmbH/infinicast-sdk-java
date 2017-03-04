package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.Action;
import io.infinicast.ThreadPool;
public class HandlerPool {
    public void queueHandlerCall(final Action call) {
        ThreadPool.QueueUserWorkItem((state) -> {
            call.accept();
            ;
        }
        );
    }
    public void destroy() {
    }
}
