package com.iadoy.muitlthread.basic.use;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class YieldDemo {
    public static final int MAX_TURN = 1000; //两个线程总共循环100次
    public static AtomicInteger index = new AtomicInteger(0); //记录两个线程循环总次数
    private static Map<String, AtomicInteger> metric = new HashMap<>(); //记录两个线程各自的循环次数

    private static void printMetric(){
        log.info("metric: " + metric);
    }

    static class YieldThread extends Thread{
        static int threadSeqNum = 1;
        public YieldThread(){
            super("yieldThread-" + threadSeqNum++);
            metric.put(this.getName(), new AtomicInteger(0));
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_TURN && index.get() < MAX_TURN; i++){
                log.info("priority: " + getPriority());
                index.incrementAndGet();
                metric.get(this.getName()).incrementAndGet();
                //每循环两次就放弃CPU使用权
                if (i % 2 == 0){
                    Thread.yield();
                }
            }
            printMetric();
            log.info(getName() + " is finished.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new YieldThread();
        thread1.setPriority(Thread.MAX_PRIORITY);
        Thread thread2 = new YieldThread();
        thread2.setPriority(Thread.MIN_PRIORITY);
        log.info("start two threads.");
        thread1.start();
        thread2.start();
        //两个线程总是能够获得差不多的运行机会，即便这里使用Thread.sleep()代替
        //CPU: i7-10700，JDK：Oracle Openjdk 1.8.0_311
        //我猜测是由于CPU核心数比较多导致的，这会使得两个线程yield的时候总是能立即获得CPU使用权
        LockSupport.parkNanos(100 * 1000L * 1000L);
    }
}
