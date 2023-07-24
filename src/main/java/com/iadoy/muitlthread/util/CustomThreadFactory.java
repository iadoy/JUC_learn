package com.iadoy.muitlthread.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义ThreadFactory
 */
public class CustomThreadFactory implements ThreadFactory {
    //线程池数量
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;

    //线程数量
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String threadTag;

    public CustomThreadFactory(String threadTag) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.threadTag = "apppool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
    }

    @Override
    public Thread newThread(Runnable target) {
        Thread t = new Thread(group, target,
                threadTag + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
