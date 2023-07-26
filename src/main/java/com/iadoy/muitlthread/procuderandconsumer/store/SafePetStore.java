package com.iadoy.muitlthread.procuderandconsumer.store;

import com.iadoy.muitlthread.procuderandconsumer.Consumer;
import com.iadoy.muitlthread.procuderandconsumer.Producer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 安全的宠物商店
 * 包含数据缓冲区、生产动作、消费动作
 */
public class SafePetStore {
    //创建一个数据缓冲区，Integer就代表商品id吧，懒得写一个类了
    private static SafeDataBuffer<Integer> SafeDataBuffer = new SafeDataBuffer<>();

    private static AtomicInteger baseId = new AtomicInteger(0);

    //生产动作
    static Callable<Integer> produceAction = () -> {
        Integer produceId = baseId.incrementAndGet();
        SafeDataBuffer.add(produceId);
        return produceId;
    };

    //消费动作
    static Callable<Integer> consumerAction = () -> {
        Integer producerId = SafeDataBuffer.fetch();
        return producerId;
    };

    public static void main(String[] args) {
        System.setErr(System.out);

        final int THREAD_TOTAL = 20;
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_TOTAL);

        //向线程池中提交生产者、消费者各5个
        //生产者速度大于消费者速度
        for (int i = 0; i < 5; i++){
            pool.submit(new Producer(produceAction, 500));
            pool.submit(new Consumer(consumerAction, 1500));
        }
    }
}
