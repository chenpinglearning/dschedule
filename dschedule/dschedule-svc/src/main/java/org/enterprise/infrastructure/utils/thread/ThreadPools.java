package org.enterprise.infrastructure.utils.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPools {
    public static ThreadPoolExecutor timeWheelThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 70,
            30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
}
