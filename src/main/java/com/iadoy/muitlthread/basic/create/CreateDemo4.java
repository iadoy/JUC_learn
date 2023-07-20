package com.iadoy.muitlthread.basic.create;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 创建线程方法4：线程池
 */
@Slf4j
public class CreateDemo4 {
    public static final int MAX_TURN = 5;
    public static final int COMPUTE_TIMES = 10_000_000;
    //创建一个拥有3个线程的线程池
    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    //定义一个Runnable实现类
    static class DemoThread implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < MAX_TURN; i++){
                log.info(Thread.currentThread().getName() + "-" + i);
                Thread.sleep(10);
            }
        }
    }

    //定义一个Callable实现类
    static class ReturnableTask implements Callable<Long> {

        @Override
        public Long call() throws Exception {
            long start = System.currentTimeMillis();
            log.info(Thread.currentThread().getName() + " is started.");
            for (int i = 0; i < COMPUTE_TIMES; i++){
                int j = i * 1234;
            }
            log.info(Thread.currentThread().getName() + " is finished.");
            return System.currentTimeMillis() - start;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //由线程池执行Runnable实例
        pool.execute(new DemoThread());
        //向线程池提交Callable实例，并获得对应的Future对象
        Future<Long> future = pool.submit(new ReturnableTask());
        //从Future对象中获取异步执行的结果
        log.info("异步结果：{}", future.get());
    }
}
