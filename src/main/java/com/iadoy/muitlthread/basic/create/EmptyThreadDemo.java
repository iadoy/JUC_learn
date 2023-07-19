package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建并运行一个空线程
 */
@Slf4j
public class EmptyThreadDemo {
    public static void main(String[] args) {

//      Thread类中线程代码逻辑的入口方法run()会执行内部变量target的run()方法
//      这里创建的空线程实例中的target为null
//      因此空线程实际上不会执行任何代码
        Thread thread = new Thread();
        log.info("current Thread name    : {}", thread.getName());
        log.info("current Thread id      : {}", thread.getId());
        log.info("current Thread state   : {}", thread.getState());
        log.info("current Thread priority: {}", thread.getPriority());

        log.info("{} is end", Thread.currentThread().getName());

        thread.start();
    }
}
