package io.infinicast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ocean on 23.03.2017.
 */
public class HandlerPool {
    private ExecutorService sExecutor = Executors.newSingleThreadExecutor();

    public void Destroy() {
        if (sExecutor != null) {
            sExecutor.shutdown();
            sExecutor = null;
        }
    }

    public void QueueHandlerCall(final Action action) {
        sExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        action.accept();
                    }
                });

    }

}
