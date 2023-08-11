package com.iadoy.cas;

import com.iadoy.muitlthread.util.JvmUtil;
import com.iadoy.muitlthread.util.MixedTargetThreadPoolLazyHolder;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class TestCompareAndSwap {
    /**
     * 基于CAS无锁实现的安全自增
     */
    static class OptimisticLockingPlus{
        //线程数量
        private static final int THREAD_COUNT = 10;
        //内部值，使用volatile保证线程可见性
        private volatile int value;
        //Unsafe实例
        private static final Unsafe UNSAFE = JvmUtil.getUnsafe();
        //value的偏移量（相对对象头的偏移量），执行CAS操作时需要
        private static final long valueOffset;
        //统计失败的次数
        private static final AtomicLong failure = new AtomicLong(0);

        static{
            try {
                valueOffset = UNSAFE.objectFieldOffset(OptimisticLockingPlus.class.getDeclaredField("value"));
                log.info("value offset: {}", valueOffset);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new Error(e);
            }
        }

        //通过CAS原子操作，进行“比较并交换”
        public final boolean unsafeCompareAndSwapInt(int oldValue, int newValue){
            //原子操作：使用unsafe的“比较并交换”方法进行value属性的交换
            return UNSAFE.compareAndSwapInt(this, valueOffset, oldValue, newValue);
        }

        //使用无锁编程实现安全的自增方法
        public void selfPlus(){
            int oldValue;
            int i = 0;
            //通过CAS原子操作，如果操作失败就自旋，一直到操作成功
            do {
                // 获取旧值
                oldValue = value;
                //统计无效的自旋次数
                if (i++ > 1){
                    failure.incrementAndGet();
                }
            }while (!unsafeCompareAndSwapInt(oldValue, oldValue + 1));
        }

        public static void main(String[] args) throws InterruptedException {
            final OptimisticLockingPlus cas = new OptimisticLockingPlus();
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
            for (int i = 0; i < THREAD_COUNT; i++){
                MixedTargetThreadPoolLazyHolder.getInnerExecutor().submit(() -> {
                    for (int j = 0; j < 1000; j++){
                        cas.selfPlus();
                    }
                    latch.countDown();
                });
            }
            latch.await();
            log.info("sum: {}", cas.value);
            log.info("failure: {}", failure.get());
        }
    }
}
