package com.iadoy.muitlthread.basic.create3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 【示例代码】Executors创建单线程线程池
 */
@Slf4j
public class CreateThreadPoolDemo {
    public static final int SLEEP_GAP = 500;

    static class TargetTask implements Runnable{
        static AtomicInteger taskNo = new AtomicInteger(1);
        private String taskName;

        public TargetTask(){
            taskName = "task-" + taskNo.getAndIncrement();
        }

        @SneakyThrows
        @Override
        public void run() {
            log.info("Task: {} doing.", taskName);
            Thread.sleep(SLEEP_GAP);
            log.info("{} done.", taskName);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //创建一个单线程的线程池
        ExecutorService pool = Executors.newSingleThreadExecutor();
        //五轮循环，每轮执行两个任务
        for (int i = 0; i < 5; i++){
            pool.execute(new TargetTask());
            pool.submit(new TargetTask());
        }
        Thread.sleep(100000);
        pool.shutdown();
    }

}
