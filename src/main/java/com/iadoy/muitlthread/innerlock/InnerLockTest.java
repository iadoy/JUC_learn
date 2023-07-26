package com.iadoy.muitlthread.innerlock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.CountDownLatch;

/**
 * 1. 使用 JOL 打印空对象的结构
 * 2. 偏向锁的演示
 */
@Slf4j
public class InnerLockTest {

    final int MAX_TREAD = 10;
    final int MAX_TURN = 1000;

    @Test
    public void testNoLockObject(){
        log.info(VM.current().details());

        ObjectLock objectLock = new ObjectLock();
        log.info("print object");
        objectLock.printSelf();
    }

    @Test
    public void testBiasedLock() throws InterruptedException {
        log.info(VM.current().details());
        //JVM延迟偏向锁
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
}
