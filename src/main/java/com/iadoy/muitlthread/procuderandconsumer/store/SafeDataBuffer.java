package com.iadoy.muitlthread.procuderandconsumer.store;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 安全的数据缓冲区
 * @param <T>
 */
@Slf4j
public class SafeDataBuffer<T> {
    public static final int MAX_AMOUNT = 10;
    private List<T> dataList = new LinkedList<>();
    //保存数量
    private AtomicInteger amount = new AtomicInteger(0);

    public synchronized void add(T element) throws Exception {
        if (amount.get() >= MAX_AMOUNT){
            log.info("队列已经满了");
            return;
        }
        dataList.add(element);
        log.info("加入元素{}", element);
        // 更新数据缓冲区数量
        amount.incrementAndGet();

        if (amount.get() != dataList.size()){
            throw new Exception("数据不一致，" + dataList.size() + " != " + amount.get());
        }
    }

    public synchronized T fetch() throws Exception {
        if (amount.get() <= 0){
            log.info("队列已经空了");
            return null;
        }
        T element = dataList.remove(0);
        log.info("取出数据{}", element);
        amount.decrementAndGet();

        if (amount.get() != dataList.size()){
            throw new Exception("数据不一致，" + dataList.size() + " != " + amount.get());
        }
        return element;
    }
}
