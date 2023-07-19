package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程方式1：继承Thread类
 */
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
                log.info(getName() + "-" + i);
            }
            //这里可以直接调用Thread父类的方法获取线程名称
            log.info(getName() + " is finished.");
        }
    }

    public static void main(String[] args) {
        Thread thread;
        for (int i = 0; i < 2; i++){
            thread = new DemoThread();
            thread.start();
        }
        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
