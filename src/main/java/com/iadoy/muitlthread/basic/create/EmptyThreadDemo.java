package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建并运行一个空线程
 */
@Slf4j
public class EmptyThreadDemo {
    public static void main(String[] args) {
        Thread thread = new Thread();
        log.info("current Thread name    : {}", thread.getName());
        log.info("current Thread id      : {}", thread.getId());
        log.info("current Thread state   : {}", thread.getState());
        log.info("current Thread priority: {}", thread.getPriority());

        log.info("{} is end", Thread.currentThread().getName());

        thread.start();
    }
}
