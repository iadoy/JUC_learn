package com.iadoy.muitlthread.basic.create;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过实现Runnable接口的方式创建线程目标类更加适合多个线程
 * 的代码逻辑去共享计算和处理同一个资源的场景。
 *
 * 下面是上述语句的一个例子
 */
@Slf4j
public class SalesDemo {
    public static final int MAX_AMOUNT = 5; //商品数量

    /**
     * 商店商品类（销售线程类），一个商品一个销售线程，每个线程异步销售4次
     */
    static class StoreGoods extends Thread{
        StoreGoods(String name){
            super(name);
        }

        private int goodsAmount = MAX_AMOUNT;

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i <= MAX_AMOUNT; i++){
                if (this.goodsAmount > 0){
                    log.info(getName() + " sales 1 good, {} left.", --goodsAmount);
                    sleep(10);
                }
            }
            log.info(getName() + " is finished.");
        }
    }

    /**
     * 商场商品类（target销售线程的目标类），一个商品最多销售4次，可以多人销售
     */
    static class MallGoods implements Runnable{
        private AtomicInteger goodsAmount = new AtomicInteger(MAX_AMOUNT);

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < MAX_AMOUNT; i++){
                if (goodsAmount.get() > 0){
                    log.info(Thread.currentThread().getName() + " sales 1 good, {} left.", goodsAmount.decrementAndGet());
                    Thread.sleep(10);
                }
            }
            log.info(Thread.currentThread().getName() + " is finished.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 商店版本中，每个线程实例内部都有5个商品，线程之间互不干扰
        // 因此，两个商店店员各卖了5各商品
        log.info("商店版本的销售");
        for (int i = 1; i < 3; i++){
            new StoreGoods("店员-" + i).start();
        }
        Thread.sleep(1000);

        log.info("==============================");

        //商场版本中，虽然同样创建了两个线程，但共享同一个资源（target、Runnable 实例，即 MallGoods 实例）
        //因此 该版本是两个店员线程共同销售5各商品
        log.info("商场版本的销售");
        MallGoods mallGoods = new MallGoods();
        for (int i = 1; i < 3; i++){
            new Thread(mallGoods, "商场销售员-" + i).start();
        }

        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
