package com.iadoy.muitlthread.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
//饿汉式式单例创建线程池：用于定时任务、顺序排队执行任务
class SeqOrScheduledTargetThreadPoolLazyHolder {
    //线程池：用于定时任务、顺序排队执行任务
    static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1, new CustomThreadFactory("seq"));

    public static ScheduledThreadPoolExecutor getInnerExecutor() {
        return EXECUTOR;
    }

    static {
        log.info("线程池已经初始化");

        //JVM关闭时的钩子函数
        Runtime.getRuntime().addShutdownHook(
                new ShutdownHookThread("定时和顺序任务线程池", new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        //优雅关闭线程池
                        ThreadUtil.shutdownThreadPoolGracefully(EXECUTOR);
                        return null;
                    }
                }));
    }

}
