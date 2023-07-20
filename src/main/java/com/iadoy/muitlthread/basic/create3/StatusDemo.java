package com.iadoy.muitlthread.basic.create3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程状态示例代码
 */
@Slf4j
public class StatusDemo {
    //每个线程执行的轮次
    public static final int MAX_TURN = 5;
    //线程编号
    static int threadSeqNum = 0;
    //全局的静态线程列表
    static List<Thread> threadList = new ArrayList<>();

    //func 输出静态县城列表中所有线程的状态
    private static void printThreadStatus(){
        for (Thread t : threadList){
            log.info(t.getName() + ": status-{}", t.getState());
        }
    }

    //func 向全局静态线程列表加入线程
    private static void addStatusThread(Thread thread){
        threadList.add(thread);
    }

    static class StatusDemoThread extends Thread{
        public StatusDemoThread(){
            super("statusPrintThread-" + ++threadSeqNum);
            //将自己加入全局静态线程列表
            addStatusThread(this);
        }

        @SneakyThrows
        @Override
        public void run() {
            log.info(getName() + ": status-{}", getState());
            for (int i = 0; i < MAX_TURN; i++){
                //睡眠当前线程
                sleep(500);
                //打印所有线程状态
                printThreadStatus();
            }
            log.info(getName() + " is finished.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //将main线程加入全局列表
        addStatusThread(Thread.currentThread());
        Thread t1, t2, t3;
        t1 = new StatusDemoThread();
        log.info(t1.getName() + ": status-{}", t1.getState());
        t2 = new StatusDemoThread();
        log.info(t2.getName() + ": status-{}", t2.getState());
        t3 = new StatusDemoThread();
        log.info(t3.getName() + ": status-{}", t3.getState());

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(500);
        t3.start();

        Thread.sleep(100000);
    }
}
