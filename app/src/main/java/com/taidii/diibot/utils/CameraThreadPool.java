package com.taidii.diibot.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class CameraThreadPool {
    private final static ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(Runtime
            .getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(), 30,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    private CameraThreadPool() {
    }

    public final static void execute(Runnable run) {
        THREAD_POOL_EXECUTOR.execute(run);
    }


    public final static void shutdownNow() {
        THREAD_POOL_EXECUTOR.shutdownNow();
    }

    public final static void shutdown(){
        THREAD_POOL_EXECUTOR.shutdown();
    }

}
