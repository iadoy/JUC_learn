package com.iadoy.muitlthread.procuderandconsumer.store;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 不安全的宠物商店
 * 包含数据缓冲区、生产动作、消费动作
 */
public class NotSafePetStore {
    //创建一个数据缓冲区，Integer就代表商品id吧，懒得写一个类了
    private static NotSafeDataBuffer<Integer> notSafeDataBuffer = new NotSafeDataBuffer<>();

    private static AtomicInteger baseId = new AtomicInteger(0);

    //生产动作
    static Callable<Integer> produceAction = () -> {
        Integer produceId = baseId.incrementAndGet();
        notSafeDataBuffer.add(produceId);
        return produceId;
    };

    //消费动作
    static Callable<Integer> consumerAction = () -> {
        Integer producerId = notSafeDataBuffer.fetch();
        return producerId;
    };
}
