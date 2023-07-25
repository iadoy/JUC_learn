package com.iadoy.muitlthread.basic.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * ThreadLocal综合应用
 */
@Slf4j
public class ComprehensiveTest {
    public static void serviceMethod() throws InterruptedException {
        //睡眠500毫秒，模拟业务逻辑执行耗时
        Thread.sleep(500);

        //执行到这里时，执行一次日志记录耗时
        SpeedLog.logPoint("point-1");

        //模拟数据库操作执行耗时
        daoMethod();

        //模拟rpc操作执行耗时
        rpcMethod();
    }

    public static void rpcMethod() throws InterruptedException {
        //睡眠600毫秒，模拟rpc调用执行耗时
        Thread.sleep(600);

        //执行到这里时，执行一次日志记录耗时
        SpeedLog.logPoint("point-3 rpc");
    }

    public static void daoMethod() throws InterruptedException {
        //睡眠400毫秒，模拟数据库操作执行耗时
        Thread.sleep(400);

        //执行到这里时，执行一次日志记录耗时
        SpeedLog.logPoint("point-2 dao");
    }

    @Test
    public void test() throws InterruptedException {
        Runnable runnable = () -> {
            SpeedLog.beginSpeedLog();
            try {
                serviceMethod();
                SpeedLog.printCost();
                SpeedLog.endSpeedLog();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        //由于使用的时单元测试，这里必须等一下才能看到输出
        //如果想要省略这一句，最简单的方法时换用main()方法
        //原因在于：
        //-表象：在junit单元测试中，当创建了新线程后，单元测试并不会等待主线程下启动的新线程是否执行结束，只要主线程结束完成，单元测试就会关闭，导致主线程中启动的新线程不能顺利执行完！
        //-本质：对于junit单元测试，当@Test注解的单元测试方法执行时，实际上junit时将该方法作为参数传给了junit.textui.TestRunner类的main函数，并通过main函数进行执行
        // 当@Test注解的单元测试方法执行结束，TestRunner类的main函数将会直接调用System.exit(0)，不管是否有其它用户线程正在执行。
        Thread.sleep(5000);
    }

}
