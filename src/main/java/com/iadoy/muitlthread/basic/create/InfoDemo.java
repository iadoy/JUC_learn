package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 1. 获取线程信息的示例
 * 2. 栈帧（方法帧）的压栈与出栈
 */
@Slf4j
public class InfoDemo {

    public static void main(String[] args)  throws InterruptedException {
        log.info("current Thread name    : {}", Thread.currentThread().getName());
        log.info("current Thread id      : {}", Thread.currentThread().getId());
        log.info("current Thread state   : {}", Thread.currentThread().getState());
        log.info("current Thread priority: {}", Thread.currentThread().getPriority());

        int a = 1, b = 1;
        int c = a / b;
        anotherFun();
        Thread.sleep(10000000);
    }

    private static void anotherFun(){
        int a = 1, b = 1;
        int c = a / b;
        anotherFun2();
    }

    private static void anotherFun2(){
        int a = 1, b = 1;
        int c = a / b;
    }
}
