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
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //启动thread2，并将其合并到main线程
        Thread thread2 = new SleepThread();
        log.info("start thread2.");
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
