package com.iadoy.muitlthread.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadUtil {
    public static void shutdownThreadPoolGracefully(ExecutorService pool) {
        // 如果已经关闭，立即返回
        if (pool == null || pool.isTerminated()){
            return;
        }
        // 拒绝新任务
        pool.shutdown();
        try {
            // 等待60秒，让线程池中的任务完成
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)){
                // 60秒后不管完没完成，中断所有线程
                pool.shutdownNow();
                // 再等60秒看看
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)){
                    log.info("线程池任务未正常执行结束");
                }
            }
        } catch (InterruptedException e) {
            // 发生异常时重新关闭线程池
            pool.shutdownNow();
        }

        //如果还没有关闭
        if (!pool.isTerminated()){
            try {
                //循环1000次，每次等待10毫秒
                for (int i = 0; i < 1000; i++){
                    if (pool.awaitTermination(100, TimeUnit.MILLISECONDS)){
                        break;
                    }
                    pool.shutdownNow();
                }
            } catch (Throwable e){
                log.error(e.getMessage());
            }
        }
    }
}
