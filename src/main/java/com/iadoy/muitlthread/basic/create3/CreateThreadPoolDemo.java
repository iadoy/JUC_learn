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

    /**
     * 演示线程池的不合理配置导致任务无法执行
     */
    @Test
    public void testWrongConfigExecutor() throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                100,
                100,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100)
        );
        for (int i = 0; i < 5; i++){
            final int taskIndex = i;
            pool.execute(() -> {
                log.info("task index={}", taskIndex);
                try {
                    //模拟线程长期无法完成
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        //每秒输出一次线程池的状态
        while (true){
            log.info("active count:{}, task count:{}", pool.getActiveCount(), pool.getTaskCount());
            Thread.sleep(1000);
        }
    }

    public class MyThreadFactory implements ThreadFactory{
        AtomicInteger threadNo = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable target) {
            String threadName = "myThread-" + threadNo.incrementAndGet();
            //创建并配置线程
            Thread thread = new Thread(target, threadName);
            thread.setPriority(8);
            log.info("创建一个线程：{}", threadName);
            return thread;
        }
    }

    /**
     * 演示使用自定义的ThreadFactory创建线程池
     * @throws InterruptedException
     */
    @Test
    public void testCustomThreadFactory() throws InterruptedException {
        //使用自定义的线程工厂创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(2, new MyThreadFactory());
        for (int i = 0; i < 5; i++){
            pool.submit(new TargetTask());
        }
        Thread.sleep(10 * 1000);
        log.info("关闭线程池");
        pool.shutdown();
    }

    /**
     * 演示ThreadPoolExecutor的三个钩子函数
     */
    //线程本地变量,用于记录线程异步任务的开始执行时间
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    @Test
    public void testHooks() throws InterruptedException {
        //这里创建的不是ThreadPoolExecutor，是匿名内部类，重写了三个方法
        ExecutorService pool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2)) {

            //继承：调度器终止钩子
            @Override
            protected void terminated() {
                log.info("调度器已经终止!");
            }

            //继承：执行前钩子
            @Override
            protected void beforeExecute(Thread t, Runnable target) {
                log.info("前钩被执行!");
                //记录开始执行时间
                START_TIME.set(System.currentTimeMillis());
                super.beforeExecute(t, target);
            }

            //继承：执行后钩子
            @Override
            protected void afterExecute(Runnable target, Throwable t) {
                super.afterExecute(target, t);
                //计算执行时长
                long time = (System.currentTimeMillis() - START_TIME.get()) ;
                log.info("{}后钩被执行, 任务执行时长（ms）：{}", target, time);
                //清空本地变量
                START_TIME.remove();
            }
        };

        for (int i = 1; i <= 5; i++) {
            pool.execute(new TargetTask());
        }
        //等待10秒
        Thread.sleep(10000);
        log.info("结束");
        pool.shutdown();
    }

    /**
     * 自定义拒绝策略，拒绝时输出条日志
     */
    class MyRejectHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.warn("task {} is discarded. task count: {}", r, executor.getTaskCount());
        }
    }

    @Test
    public void testCustomReject() throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2), new MyThreadFactory(), new MyRejectHandler());
        //预启动所有核心线程
        pool.prestartAllCoreThreads();
        for (int i = 0; i < 10; i++){
            pool.execute(new TargetTask());
        }
        Thread.sleep(10000);
        log.info("关闭线程池");
        pool.shutdown();
    }
}
