package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程方式3：使用Callable和Future
 * 方法执行顺序：
 * Thread.run() -> target.run() -> Callable.call()
 * 这里的target实际上就是FutureTask，Callable.call()的返回值保存在FutureTask的outcome中
 */
@Slf4j
public class CreateDemo3 {
    public static final int MAX_TURN = 5;
    public static final int COMPUTE_TIMES = 10_000_000;

    //创建一个Callable接口的实现类
    static class ReturnableTask implements Callable<Long> {

        //异步执行的逻辑：返回计算一千万次乘法所需的时间
        @Override
        public Long call() throws Exception {
            long startTime = System.currentTimeMillis();
            log.info(Thread.currentThread().getName() + " is started.");
            Thread.sleep(1000);

            for (int i = 0; i < COMPUTE_TIMES; i++){
                int j = i * 10000;
            }

            long interval = System.currentTimeMillis() - startTime;
            log.info(Thread.currentThread().getName() + "is finished.");
            return interval;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ReturnableTask returnableTask = new ReturnableTask();
        //将自定义的Callable实例放入FutureTask中
        FutureTask<Long> futureTask = new FutureTask<>(returnableTask);
        //FutureTask实例作为Thread的target
        Thread thread = new Thread(futureTask, "returnableThread");
        thread.start();

        Thread.sleep(500);

        log.info(Thread.currentThread().getName() + "让子弹飞一会");
        log.info(Thread.currentThread().getName() + "做一点自己的事情");
        for (int i = 0; i < COMPUTE_TIMES / 2; i++){
            int j = i * 10000;
        }
        log.info(Thread.currentThread().getName() + "获取异步任务的执行结果");
        log.info(thread.getName() + "线程计算时间： {}", futureTask.get());
        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
