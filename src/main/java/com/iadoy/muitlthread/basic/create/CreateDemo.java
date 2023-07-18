package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateDemo {

    public static final int MAX_TURN = 5;
    public static int threadNo = 1;

    static class DemoThread extends Thread{
        public DemoThread(){
            super("DemoThread-" + threadNo++);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_TURN; i++){
                log.info(Thread.currentThread().getName() + "-" + i);
            }
            log.info(Thread.currentThread().getName() + " is end.");
        }
    }

    public static void main(String[] args) {
        Thread thread;
        for (int i = 0; i < 2; i++){
            thread = new DemoThread();
            thread.start();
        }
        log.info(Thread.currentThread().getName() + " is end.");
    }
}
