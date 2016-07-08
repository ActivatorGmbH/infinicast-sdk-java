package io.infinicast.client.impl.messaging.receiver;
import io.infinicast.Action;
import io.infinicast.ThreadPool;
import io.infinicast.*;

public class HandlerPool {
    public void queueHandlerCall(Action call) {
        ThreadPool.QueueUserWorkItem((state) -> {
            call.accept();
            ;
        }
        );
    }
}
