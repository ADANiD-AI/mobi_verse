package com.mobiverse.MobiVerse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {

    private ExecutorService executorService;

    public TaskManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public void executeTask(Runnable task) {
        executorService.execute(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
