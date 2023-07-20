package com.iadoy.muitlthread.basic.use;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程中断示例代码
 */
@Slf4j
public class InterruptDemo {
    public static final int SLEEP_GAP = 5000; //睡眠时长

    static class SleepThread extends Thread{
        static int threadSeqNum = 1;

        public SleepThread(){
            super("sleepThread-" + threadSeqNum++);
        }

        @Override
        public void run() {
            log.info(getName() + " start sleeping.");
            try {
                sleep(SLEEP_GAP);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info(getName() + " is interrupted.");
                return;
            }
            log.info(getName() + " is finished.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new SleepThread();
        Thread t2 = new SleepThread();
        t1.start();
        t2.start();

        Thread.sleep(2000); //主线程等待2秒
        log.info("interrupt t1");
        t1.interrupt(); //终端t1
        Thread.sleep(5000); //主线程等待5秒
        log.info("interrupt t2");
        t2.interrupt(); //终端t2
        Thread.sleep(1000);
        log.info(Thread.currentThread().getName() + " is finished.");
    }

    /**
     * 使用isInterrupted()状态标志位结束线程示例
     * @throws InterruptedException
     */
    @Test
    public void testInterrupt() throws InterruptedException {
        Thread t = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("start");
                while (true){
                    log.info("{}", isInterrupted());

                    //如果这里用Thread.sleep()的话无法得到预想的结果
                    //会抛出InterruptedException异常
                    //两者的区别现在还不懂 TODO
                    LockSupport.parkNanos(5000 * 1000L * 1000L);
                    if (isInterrupted()){
                        log.info(getName() + " is interrupted, goodbye~");
                        return;
                    }
                }
            }
        };
        t.start();

        //主线程等待两秒后中断线程t
        Thread.sleep(2000);
        t.interrupt();
        Thread.sleep(2000);
        t.interrupt();
    }
}
