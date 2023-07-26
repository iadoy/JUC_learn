package com.iadoy.muitlthread.basic.use;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * wait()，notify()线程通信演示
 */
@Slf4j
public class WaitNotifyDemo {
    static Object locko = new Object();

    //等待线程的异步目标任务

    static class WaitTarget implements Runnable{

        @Override
        public void run() {
            synchronized (locko){
                try {
                    log.info("启动等待");
                    //等待被通知，同时释放locko监视器的Owner权
                    locko.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取到监视器的Owner权利
                log.info("收到通知，当前线程继续执行");
            }
        }
    }

    static class NotifyTarget implements Runnable{

        @Override
        public void run() {
            synchronized (locko){
                //从屏幕读取输入，目的是阻塞通知线程，方便使用jstack查看线程状态
                Scanner sc = new Scanner(System.in);
                System.out.println("Please Enter sth:");
                String nextLine = sc.nextLine();  //读取字符串型输入
                sc.close();

                //获取lock锁，然后进行发送
                // 此时不会立即释放locko的Monitor的Owner，需要执行完毕
                locko.notifyAll();
                log.info("发出通知了，但是线程还没有立马释放锁");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new WaitTarget(), "waitThread");
        Thread notifyThread = new Thread(new NotifyTarget(), "notifyThread");

        //启动等待线程
        waitThread.start();
        Thread.sleep(1000);
        //启动通知线程
        notifyThread.start();
    }
}
