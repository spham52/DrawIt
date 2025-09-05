package com.drawit.demo.model;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ScheduledTask {

    private final ScheduledExecutorService executor;
    private final ScheduledFuture<?> future;

    public ScheduledTask(ScheduledExecutorService executor, ScheduledFuture<?> future) {
        this.executor = executor;
        this.future = future;
    }

    public void cancel() {
        future.cancel(false);
        executor.shutdown();
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }
}
