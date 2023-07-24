package com.iadoy.muitlthread.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建IO密集型线程池示例代码
 */
public class IoIntenseTargetThreadPoolLazyHolder {
    //CPU核心数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //IO处理线程数，一般取CPU核心数的2倍
    public static final int IO_MAX = Math.max(2, CPU_COUNT * 2);
    //空闲保活时长，单位秒
    public static final int KEEP_ALIVE_SECONDS = 30;
    //有界队列的大小
    public static final int QUEUE_SIZE = 128;

    //饿汉式单例直接创建线程池
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            IO_MAX,
            IO_MAX,
            KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(QUEUE_SIZE),
            new CustomThreadFactory("io")
    );

    //静态代码块注册JVM钩子函数
    static {
        //允许核心线程超时关闭
        EXECUTOR.allowCoreThreadTimeOut(true);
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("IO密集型任务线程池", () -> {
            ThreadUtil.shutdownThreadPoolGracefully(EXECUTOR);
            return null;
        }));
    }
}
