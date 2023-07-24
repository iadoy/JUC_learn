package com.iadoy.muitlthread.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建混合型型线程池示例代码
 */
public class MixedTargetThreadPoolLazyHolder {
    //最大线程数
    private static final int MIXED_MAX = 128;
    //配置文件中设置线程数量的关键字
    public static final String MIXED_THREAD_AMOUNT_KEY = "mixed.thread.amount";
    //首先从环境变量 mixed.thread.amount 中获取预先配置的线程数
    //如果没有对 mixed.thread.amount 做配置，则使用常量 MIXED_MAX 作为线程数
    public static final int MIXED_THREAD_AMOUNT = null != System.getProperty(MIXED_THREAD_AMOUNT_KEY) ? Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT_KEY)) : MIXED_MAX;
    //空闲保活时长，单位秒
    public static final int KEEP_ALIVE_SECONDS = 30;
    //有界队列的大小
    public static final int QUEUE_SIZE = 128;

    //饿汉式单例直接创建线程池
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            MIXED_THREAD_AMOUNT,
            MIXED_THREAD_AMOUNT,
            KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(QUEUE_SIZE),
            new CustomThreadFactory("mixed")
    );

    //静态代码块注册JVM钩子函数
    static {
        //允许核心线程超时关闭
        EXECUTOR.allowCoreThreadTimeOut(true);
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("混合型型任务线程池", () -> {
            ThreadUtil.shutdownThreadPoolGracefully(EXECUTOR);
            return null;
        }));
    }

    public static ThreadPoolExecutor getInnerExecutor() {
        return EXECUTOR;
    }
}
