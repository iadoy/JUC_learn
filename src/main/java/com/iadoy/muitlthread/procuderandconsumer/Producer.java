package com.iadoy.muitlthread.procuderandconsumer;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Producer implements Runnable {

    //生产的时间间隔
    public static final int PRODUCER_GAP = 200;
    //生产总次数
    static final AtomicInteger TURN = new AtomicInteger(0);
    //生产者编号
    static final AtomicInteger PRODUCER_NO = new AtomicInteger(1);

    //生产者名称
    String name;
    //生产动作
    Callable action;
    int gap = PRODUCER_GAP;

    public Producer(Callable action, int gap){
        this.action = action;
        this.gap = gap;
        this.name = "Producer-" + PRODUCER_NO.getAndIncrement();
    }

    @Override
    public void run() {
        while (true){
            try {
                TURN.incrementAndGet();
                //执行生产动作
                Object o = action.call();
                //输出生产结果
                if (o != null){
                    log.info("第{}次生产结果：{}", TURN.get(), o);
                }
                Thread.sleep(gap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
