package com.iadoy.muitlthread.basic.use;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepDemo {
    public static final int SLEEP_GAP = 5000; //睡眠时长
    public static final int MAX_TURN = 50;  //多跑几轮，方便jstack命令查看线程状态

    static class SleepThread extends Thread{
        static int threadSeqNum = 1;

        public SleepThread(){
            super("sleepThread-" + threadSeqNum++);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_TURN; i++){
                log.info(getName() + " sleep turns-{}", i);
                try {
                    Thread.sleep(SLEEP_GAP);
                } catch (InterruptedException e) {
                    log.error(getName() + " is interrupted.");
                }
            }
            log.info(getName() + " is finished.");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++){
            Thread t = new SleepThread();
            t.start();
        }

        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
