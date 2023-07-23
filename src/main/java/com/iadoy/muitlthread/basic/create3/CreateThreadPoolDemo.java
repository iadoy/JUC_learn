package com.iadoy.muitlthread.basic.create3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 【示例代码】Executors创建程线程池
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

    /**
     * Executors创建单线程、固定线程、可缓存线程池
     */
    public static void main(String[] args) throws InterruptedException {
        //创建一个单线程的线程池
//        ExecutorService pool = Executors.newSingleThreadExecutor();
        //创建一个固定拥有3个线程的线程池
//        ExecutorService pool = Executors.newFixedThreadPool(3);
        //创建一个可缓存线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        //五轮循环，每轮执行两个任务
        for (int i = 0; i < 5; i++){
            pool.execute(new TargetTask());
            pool.submit(new TargetTask());
        }
        Thread.sleep(100000);
        pool.shutdown();
    }

    /**
     * Executors创建可调度线程池
     */
    @Test
    public void test() throws InterruptedException {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //五轮循环，每轮执行两个任务
        for (int i = 0; i < 2; i++){
            //4个参数分别为执行目标、延迟时间、间隔时间、时间单位
            service.scheduleAtFixedRate(new TargetTask(), 0, 500, TimeUnit.MILLISECONDS);
        }
        Thread.sleep(100000);
        service.shutdown();
    }
}
