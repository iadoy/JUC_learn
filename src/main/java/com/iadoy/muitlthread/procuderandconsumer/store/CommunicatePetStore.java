package com.iadoy.muitlthread.procuderandconsumer.store;

import com.iadoy.muitlthread.procuderandconsumer.Consumer;
import com.iadoy.muitlthread.procuderandconsumer.Producer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CommunicatePetStore {
    //数据缓冲区最大长度
    public static final int MAX_AMOUNT = 10;

    //数据缓冲区，类定义
    static class DataBuffer<T> {
        //保存数据
        private List<T> dataList = new LinkedList<>();
        //数据缓冲区长度
        private Integer amount = 0;
        private final Object LOCK_OBJECT = new Object();
        private final Object NOT_FULL = new Object();
        private final Object NOT_EMPTY = new Object();

        // 向数据区增加一个元素
        public void add(T element) throws InterruptedException {
            while (amount >= MAX_AMOUNT){
                synchronized (NOT_FULL){
                    log.info("队列已经满了！");
                    //等待未满通知
                    NOT_FULL.wait();
                }
            }
            synchronized (LOCK_OBJECT){
                dataList.add(element);
                amount++;
            }
            synchronized (NOT_EMPTY){
                //发送未空通知,唤醒消费者
                NOT_EMPTY.notify();
            }
        }

        //从数据区取出一个商品
        public T fetch() throws InterruptedException {
            while (amount <= 0){
                synchronized (NOT_EMPTY){
                    log.info("队列已经空了！");
                    //等待未空通知
                    NOT_EMPTY.wait();
                }
            }
            T element;
            synchronized (LOCK_OBJECT){
                element = dataList.remove(0);
                amount--;
            }
            synchronized (NOT_FULL){
                //发送未满通知
                NOT_FULL.notify();
            }
            return element;
        }
    }

    public static void main(String[] args) {
        AtomicInteger generator = new AtomicInteger(0);
        //共享数据区，实例对象
        DataBuffer<Integer> dataBuffer = new DataBuffer<>();
        //生产者执行的动作
        Callable<Integer> produceAction = () -> {
            //首先生成一个商品
            int num = generator.incrementAndGet();
            dataBuffer.add(num);
            return num;
        };
        //消费者执行的动作
        Callable<Integer> consumeAction = () -> dataBuffer.fetch();

        final int THREAD_TOTAL = 20;
        //线程池，用于多线程模拟测试
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_TOTAL);
        //假定共11个线程，其中有10个消费者，但是只有1个生产者
        final int CONSUMER_TOTAL = 11;
        final int PRODUCER_TOTAL = 1;

        for (int i = 0; i < PRODUCER_TOTAL; i++){
            //生产者线程每生产一个商品，间隔50毫秒
            pool.submit(new Producer(produceAction, 50));
        }
        for (int i = 0; i < CONSUMER_TOTAL; i++){
            //消费者线程每消费一个商品，间隔100毫秒
            pool.submit(new Consumer(consumeAction, 100));
        }
    }
}
