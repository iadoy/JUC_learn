package com.iadoy.muitlthread.basic.create;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程方式2：实现Runnable接口
 * 这里RunTarget类是静态内部类，但这是为了配合main方法而写的静态类，实际上写成外部类也可以。
 * 除此之外，还可以鞋厂匿名内部类、Lambda表达式等
 */
@Slf4j
public class CreateDemo2 {
    public static final int MAX_TURN = 5;
    static int threadNo = 1;

    //静态内部类实现Runnable接口
    static class RunTarget implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < MAX_TURN; i++){
                log.info(Thread.currentThread().getName() + "-" + i);
            }
            //由于Runnable实例没有继承Thread类，本身不知道会由哪个线程执行，因此这里需要获取到当前线程才能得到对应的线程名称
            log.info(Thread.currentThread().getName() + " is finished.");
        }
    }

    public static void main(String[] args) {
        Thread thread;
        for (int i = 0; i < 2; i++){
            //在构造方法中给线程提供target和线程名称
            thread = new Thread(new RunTarget(), "RunnableThread-" + threadNo++);
            thread.start();
        }
        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
