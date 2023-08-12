package com.iadoy.cas;

import com.iadoy.muitlthread.util.CpuIntenseTargetThreadPoolLazyHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class LongAdderVSAtomicLongTest {
    final int TURNS = 100_000_000;

    //对比测试用例一：调用AtomicLong完成10个线程累加
    @Test
    public void testAtomicLong() throws InterruptedException {
        //并发任务数
        final int TASK_AMOUNT = 10;

        //线程池，获取CPU密集型任务线程池
        ExecutorService pool = CpuIntenseTargetThreadPoolLazyHolder.getInnerExecutor();

        //定义一个原子对象
        AtomicLong atomicLong = new AtomicLong(0);

        //线程同步倒数闩
        CountDownLatch countDownLatch = new
                CountDownLatch(TASK_AMOUNT);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASK_AMOUNT; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < TURNS; j++) {
                        atomicLong.incrementAndGet();
                    }
                    // Print.tcfo("本线程累加完成");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //倒数闩，倒数一次
                countDownLatch.countDown();
            });
        }

        //等待倒数闩完成所有的倒数操作
        countDownLatch.await();
        float time = (System.currentTimeMillis() - start) / 1000F;
        //输出统计结果
        log.info("运行时长：{}", time);
        log.info("sum: {}", atomicLong.get());
    }

    //对比测试用例二：调用LongAdder完成10个线程累加1000次
    @Test
    public void testLongAdder() throws InterruptedException {
        //并发任务数
        final int TASK_AMOUNT = 10;

        //线程池，获取CPU密集型任务线程池
        ExecutorService pool = CpuIntenseTargetThreadPoolLazyHolder.getInnerExecutor();

        //定义一个LongAdder 对象
        LongAdder longAdder = new LongAdder();

        //线程同步倒数闩
        CountDownLatch countDownLatch = new
                CountDownLatch(TASK_AMOUNT);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASK_AMOUNT; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < TURNS; j++)
                    {
                        longAdder.add(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //倒数闩，倒数一次
                countDownLatch.countDown();
            });
        }
        //等待倒数闩完成所有的倒数操作
        countDownLatch.await();
        float time = (System.currentTimeMillis() - start) / 1000F;
        //输出统计结果
        log.info("运行时长：{}", time);
        log.info("sum: {}", longAdder.longValue());
    }

}
