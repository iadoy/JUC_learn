package com.iadoy.muitlthread.innerlock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.CountDownLatch;

/**
 * 1. 使用 JOL 打印空对象的结构
 * 2. 偏向锁的演示
 * 3. 轻量级锁演示
 * 4. 重量级锁的演示
 */
@Slf4j
public class InnerLockTest {

    final int MAX_TREAD = 10;
    final int MAX_TURN = 1000;

    /**
     * 无锁状态演示
     */
    @Test
    public void testNoLockObject(){
        log.info(VM.current().details());

        ObjectLock objectLock = new ObjectLock();
        log.info("print object");
        objectLock.printSelf();
    }

    /**
     * 偏向锁状态演示
     */
    @Test
    public void testBiasedLock() throws InterruptedException {
        log.info(VM.current().details());
        //JVM延迟偏向锁
        //JVM会在启动后4秒才启用偏向锁，减少无谓的所操作，提高性能
        //所以这里等五秒
        Thread.sleep(5000);

        ObjectLock lock = new ObjectLock();

        log.info("抢占锁之前，锁的状态");
        lock.printStruct();
        Thread.sleep(5000);

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            for (int i = 0; i < MAX_TURN; i++){
                synchronized (lock){
                    lock.increase();
                    if (i == MAX_TURN / 2){
                        log.info("占有锁，lock的状态：");
                        lock.printStruct();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            latch.countDown();
        }, "biased_lock_demo").start();

        latch.await();
        Thread.sleep(5000);
        log.info("释放锁后，锁的状态：");
        lock.printStruct();
    }

    /**
     * 轻量级锁演示
     */
    @Test
    public void testLightweightLock() throws InterruptedException {
        log.info(VM.current().details());
        //JVM延迟偏向锁
        Thread.sleep(5000);

        ObjectLock lock = new ObjectLock();

        log.info("抢占锁之前，锁的状态");
        lock.printStruct();
        Thread.sleep(5000);

        CountDownLatch latch = new CountDownLatch(2);
        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++){
                synchronized (lock){
                    lock.increase();
                    if (i == 1){
                        log.info("第一个线程占有锁，lock的状态：");
                        lock.printStruct();
                    }
                }
            }
            latch.countDown();
            try {
                while (true){
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();

        Thread.sleep(1000);

        Runnable lightweightRunnable = () -> {
            for (int i = 0; i < MAX_TURN; i++){
                synchronized (lock){
                    lock.increase();
                    if (i == MAX_TURN / 2){
                        log.info("第二个线程抢锁，lock的状态：");
                        lock.printStruct();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            latch.countDown();
        };
        new Thread(lightweightRunnable).start();

        latch.await();
        Thread.sleep(2000);
        log.info("释放锁后，锁的状态：");
        lock.printStruct();
    }

    /**
     * 重量级锁演示
     */
    @Test
    public void testHeavyWeightLock() throws InterruptedException {
        log.info(VM.current().details());
        //JVM延迟偏向锁
        Thread.sleep(5000);

        ObjectLock lock = new ObjectLock();

        log.info("抢占锁之前，锁的状态");
        lock.printStruct();
        Thread.sleep(5000);

        CountDownLatch latch = new CountDownLatch(3);
        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++){
                synchronized (lock){
                    lock.increase();
                    if (i == 0){
                        log.info("第一个线程占有锁，lock的状态：");
                        lock.printStruct();
                    }
                }
            }
            latch.countDown();
            //让线程一直运行
            try {
                while (true){
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();

        Thread.sleep(1000);

        Runnable heavyweightRunnable = () -> {
            for (int i = 0; i < MAX_TURN; i++){
                synchronized (lock){
                    lock.increase();
                    if (i == 0){
                        log.info("又有线程抢锁，lock的状态：");
                        lock.printStruct();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            latch.countDown();
        };
        //再启动两个线程竞争锁
        new Thread(heavyweightRunnable).start();
        Thread.sleep(100);
        new Thread(heavyweightRunnable).start();

        latch.await();
        Thread.sleep(2000);
        log.info("释放锁后，锁的状态：");
        lock.printStruct();
    }
}
