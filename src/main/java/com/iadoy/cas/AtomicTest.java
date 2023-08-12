package com.iadoy.cas;

import com.iadoy.muitlthread.util.MixedTargetThreadPoolLazyHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j
public class AtomicTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 10; i++){
            MixedTargetThreadPoolLazyHolder.getInnerExecutor().submit(() -> {
                for (int j = 0; j < 1000; j++){
                    atomicInteger.incrementAndGet();
                }
                latch.countDown();
            });
        }
        latch.await();
        log.info("sum: {}", atomicInteger.get());
    }

    @Test
    public void testAtomicIntegerArray(){
        int tempValue = 0;
        int[] array = {1,2,3,4,5,6};
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(array);
        tempValue = atomicIntegerArray.getAndSet(0, 2);
        log.info("tempValue:{}, atomicIntegerArray:{}", tempValue, atomicIntegerArray);

        tempValue = atomicIntegerArray.getAndIncrement(0);
        log.info("tempValue:{}, atomicIntegerArray:{}", tempValue, atomicIntegerArray);

        tempValue = atomicIntegerArray.getAndAdd(0, 5);
        log.info("tempValue:{}, atomicIntegerArray:{}", tempValue, atomicIntegerArray);
    }

    @Test
    public void testAtomicStampedReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 0);

        MixedTargetThreadPoolLazyHolder.getInnerExecutor().submit(() -> {
            boolean success = false;
            int stamp = atomicStampedReference.getStamp();
            log.info("before sleep 500: value = {}, stamp = {}", atomicStampedReference.getReference(), atomicStampedReference.getStamp());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            success = atomicStampedReference.compareAndSet(1, 10, stamp, stamp++);
            log.info("after sleep 500[1]: success = {} value = {}, stamp = {}", success, atomicStampedReference.getReference(), atomicStampedReference.getStamp());

            success = atomicStampedReference.compareAndSet(10, 1, stamp, stamp + 1);
            log.info("before sleep 500[2]: success = {} value = {}, stamp = {}", success, atomicStampedReference.getReference(), atomicStampedReference.getStamp());

            latch.countDown();
        });

        MixedTargetThreadPoolLazyHolder.getInnerExecutor().submit(() -> {
            boolean success = false;
            int stamp = atomicStampedReference.getStamp();
            log.info("before sleep 1000: value = {}, stamp = {}", atomicStampedReference.getReference(), atomicStampedReference.getStamp());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            success = atomicStampedReference.compareAndSet(1, 20, stamp, 1 + stamp);

            stamp++;
            log.info("before sleep 1000[3]: success = {} value = {}, stamp = {}", success, atomicStampedReference.getReference(), atomicStampedReference.getStamp());

            latch.countDown();
        });
        latch.await();

    }

//    private boolean getMark(AtomicMarkableReference<Integer> ref){
//        boolean[] markHolder = {false};
//        int value = ref.get(markHolder);
//        return markHolder[0];
//    }

    @Test
    public void testAtomicMarkableReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference<>(1, false);

        MixedTargetThreadPoolLazyHolder.getInnerExecutor().submit(() -> {
            boolean success = false;
            int value = atomicMarkableReference.getReference();
            boolean mark = atomicMarkableReference.isMarked();
            log.info("before sleep 500: value = {}, mark = {}", value, mark);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            success = atomicMarkableReference.compareAndSet(1, 10, mark, !mark);
            log.info("after sleep 500[1]: success = {} value = {}, mark = {}", success, atomicMarkableReference.getReference(), atomicMarkableReference.isMarked());

            latch.countDown();
        });

        MixedTargetThreadPoolLazyHolder.getInnerExecutor().submit(() -> {
            boolean success = false;
            int value = atomicMarkableReference.getReference();
            boolean mark = atomicMarkableReference.isMarked();
            log.info("before sleep 1000: value = {}, stamp = {}", value, mark);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            success = atomicMarkableReference.compareAndSet(1, 20, mark, !mark);

            log.info("before sleep 1000[3]: success = {} value = {}, stamp = {}", success, atomicMarkableReference.getReference(), atomicMarkableReference.isMarked());

            latch.countDown();
        });
        latch.await();
    }
}
