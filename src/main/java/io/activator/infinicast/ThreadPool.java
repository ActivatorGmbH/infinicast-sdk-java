package io.activator.infinicast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ThreadPool {
    private static final ExecutorService sExecutor= Executors.newSingleThreadExecutor();
    public static void QueueUserWorkItem(Consumer<Object> item) {
        sExecutor.execute(() -> item.accept(null));
    }
    public static void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


