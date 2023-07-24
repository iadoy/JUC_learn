package com.iadoy.muitlthread.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建CPU密集型线程池示例代码
 */
public class CpuIntenseTargetThreadPoolLazyHolder {
    //CPU核心数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //最大线程数,与CPU核心数量相等
    public static final int MAX_POOL_SIZE = CPU_COUNT;
    //空闲保活时长，单位秒
    public static final int KEEP_ALIVE_SECONDS = 30;
    //有界队列的大小
    public static final int QUEUE_SIZE = 128;

    //饿汉式单例直接创建线程池
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            MAX_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(QUEUE_SIZE),
            new CustomThreadFactory("cpu")
    );

    //静态代码块注册JVM钩子函数
    static {
        //允许核心线程超时关闭
        EXECUTOR.allowCoreThreadTimeOut(true);
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("CPU密集型任务线程池", () -> {
            ThreadUtil.shutdownThreadPoolGracefully(EXECUTOR);
            return null;
        }));
    }

    public static ThreadPoolExecutor getInnerExecutor() {
        return EXECUTOR;
    }
}
