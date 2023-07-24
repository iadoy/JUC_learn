package com.iadoy.muitlthread.basic.create3;

import ch.qos.logback.core.testUtil.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;
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
    public void testScheduledExecutor() throws InterruptedException {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //五轮循环，每轮执行两个任务
        for (int i = 0; i < 2; i++){
            //4个参数分别为执行目标、延迟时间、间隔时间、时间单位
            service.scheduleAtFixedRate(new TargetTask(), 0, 500, TimeUnit.MILLISECONDS);
        }
        Thread.sleep(100000);
        service.shutdown();
    }

    /**
     * 使用submit()向线程池提交任务，并获取Future对象
     */
    @Test
    public void testSubmit() throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        Future<Integer> future = pool.submit(() -> {
            Random r = new Random(System.currentTimeMillis());
            return r.nextInt(1000);
        });

        try {
            Integer integer = future.get();
            log.info("异步执行结果：{}", integer);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("异步调用中断");
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.error("异步调用发生异常");
        }
        Thread.sleep(10 * 1000);
        pool.shutdown();
    }

    /**
     * 演示submit()过程中发生的异常如何捕获
     */
    @Test
    public void testSubmitException() throws InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        //提交一个新的异步任务，该异步任务会抛出异常
        Future<Object> future = pool.submit(() -> {
            throw new RuntimeException("异步任务出错了");
        });

        try {
            //同样用get()方法获取，如果获取到的是执行过程中的异常，会由下面的catch捕获
            Object o = future.get();
            log.info("任务完成，结果为{}", o);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("异步调用中断");
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.error("异步调用发生异常，异常为{}", e.getMessage());
        }

        Thread.sleep(10 * 1000);
        pool.shutdown();
    }
}
