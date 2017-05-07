package io.infinicast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HandlerPool {
    private ExecutorService sExecutor = Executors.newSingleThreadExecutor();


    public void QueueHandlerCall(final Action item) {
        sExecutor.execute(
                () -> item.accept());

    }


    public void Destroy() {

        if (sExecutor != null) {
            sExecutor.shutdown();
        }
    }

}


