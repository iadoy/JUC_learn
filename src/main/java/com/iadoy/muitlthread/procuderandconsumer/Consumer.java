package com.iadoy.muitlthread.procuderandconsumer;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Consumer implements Runnable{

    //消费的时间间隔
    public static final int CONSUME_GAP = 100;
    //消费总次数
    static final AtomicInteger TURN = new AtomicInteger(0);
    //消费者编号
    static final AtomicInteger CONSUMER_NO = new AtomicInteger(1);

    //消费者名称
    String name;
    //消费动作
    Callable action;
    int gap = CONSUME_GAP;

    public Consumer(Callable action, int gap){
        this.action = action;
        this.gap = gap;
        this.name = "Consumer-" + CONSUMER_NO.getAndIncrement();
    }

    @Override
    public void run() {
        while (true){
            //增加消费次数
            TURN.incrementAndGet();
            try {
                Object o = action.call();
                if (o != null){
                    log.info("第{}次消费结果：{}", TURN.get(), o);
                }
                Thread.sleep(gap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
