package com.iadoy.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantCommunicationTest {
    //创建一个显式锁
    static Lock lock = new ReentrantLock();
    //获取一个显式锁绑定的Condition对象
    static private Condition condition = lock.newCondition();

    static class WaitTarget implements Runnable{

        @Override
        public void run() {
            lock.lock(); //加锁
            try {
                log.info("I am a waiter.");
                condition.await(); //等待另一个线程的唤醒，并释放该锁
                log.info("receive a advice, waiter continue...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); //释放锁
            }
        }
    }

    static class NotifyTarget implements Runnable{

        @Override
        public void run() {
            lock.lock(); //加锁
            try {
                log.info("I am a notifier.");
                condition.signal(); //唤醒该Condition下await的线程
                log.info("send a signal.");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock(); //释放锁，让等待线程获得锁
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new WaitTarget(), "WaitThread");
        Thread NotifyThread = new Thread(new NotifyTarget(), "NotifyThread");

        waitThread.start();
        NotifyThread.start();
    }
}
