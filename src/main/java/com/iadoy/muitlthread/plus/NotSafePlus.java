package com.iadoy.muitlthread.plus;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * 测试自增操作（++）是不是线程安全
 */
@Slf4j
public class NotSafePlus {
    private Integer amount = 0; //临界资源

    //临界代码块
    public void selfPlus(){
        amount++;
    }

    //使用synchronized关键字
    public synchronized void selfPlusSync(){
        amount++;
    }

    public Integer getAmount(){
        return amount;
    }

    public static void main(String[] args) throws InterruptedException {

        final int MAX_THREAD = 10;
        final int MAX_TURN = 1_000_000;
        final int TARGET = 10_000_000;

        CountDownLatch latch = new CountDownLatch(MAX_THREAD);
        NotSafePlus counter = new NotSafePlus();

        for (int i = 0; i < MAX_THREAD; i++){
            new Thread(() -> {
                for (int j = 0; j < MAX_TURN; j++){
//                    counter.selfPlus();
                    counter.selfPlusSync();
                }
                //倒数闩减一
                latch.countDown();
            }).start();
        }

        latch.await(); //等待倒数闩的次数减少到0，这在本例中表示所有线程执行完成

        log.info("target: {}, actually: {}, diff: {}", TARGET, counter.getAmount(), TARGET - counter.getAmount());
    }
}
