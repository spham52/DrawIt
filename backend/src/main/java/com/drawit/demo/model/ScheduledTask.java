package com.drawit.demo.model;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ScheduledTask {

    private final ScheduledExecutorService executor;
    private volatile ScheduledFuture<?> future;

    public ScheduledTask(ScheduledExecutorService executor, ScheduledFuture<?> future) {
        this.executor = executor;
        this.future = future;
    }

    public void cancel() {
        if (future != null) {
            future.cancel(true);
            executor.shutdownNow();
        }
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

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }
}
