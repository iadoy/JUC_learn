package com.iadoy.muitlthread.basic.use;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程合并示例代码
 */
@Slf4j
public class JoinDemo {
    public static final int SLEEP_GAP = 5000;

    static class SleepThread extends Thread{
        static int threadSeqNum = 1;
        public SleepThread(){
            super("sleepThread-" + threadSeqNum++);
        }

        @Override
        public void run() {
            try {
                log.info(getName() + " start sleeping.");
                sleep(SLEEP_GAP);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info(getName() + " is interrupted.");
                return;
            }
            log.info(getName() + " is finished.");
        }
    }

    public static void main(String[] args) {
        //启动thread1，并将其合并到main线程
        Thread thread1 = new SleepThread();
        log.info("start thread1.");
        thread1.start();
        try {
            //合并thread1，且不指定最长等待时间
            //这将导致main线程处于WAITING状态
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //启动thread2，并将其合并到main线程
        Thread thread2 = new SleepThread();
        log.info("start thread2.");
        thread2.start();
        try {
            //限时合并thread2
            //这将导致main线程处于TIMED_WAITING状态
            thread2.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
