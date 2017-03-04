package io.infinicast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static final ExecutorService sExecutor = Executors.newSingleThreadExecutor();

    public static void QueueUserWorkItem(final Consumer<Object> item) {
        sExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        item.accept(null);
                    }
                });

    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


