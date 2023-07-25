package com.iadoy.muitlthread.basic.threadlocal;

import com.iadoy.muitlthread.util.MixedTargetThreadPoolLazyHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadLocal的基本使用
 */
@Slf4j
public class ThreadLocalTest {
    //定义线程本地变量
    private static final ThreadLocal<Foo> LOCAL_FOO = new ThreadLocal<>();

    public static void main(String[] args) {
        // 获取自己写的混合型线程池，默认情况下有128个线程
        ThreadPoolExecutor pool = MixedTargetThreadPoolLazyHolder.getInnerExecutor();

        //提交5个线程
        for (int i = 0; i < 5; i++){
            pool.execute(() -> {
                if (LOCAL_FOO.get() == null){
                    LOCAL_FOO.set(new Foo());
                }
                log.info("init local: {}", LOCAL_FOO.get());

                //每个线程执行10次
                for (int j = 0; j < 10; j++){
                    Foo foo = LOCAL_FOO.get();
                    foo.setBar(foo.getBar() + 1);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("累加10次之后的本地值：{}", LOCAL_FOO.get());

                //清空ThreadLocal中的值
                //这对线程池中的线程来说尤为重要，因为可能干扰线程执行下一个任务
                LOCAL_FOO.remove();
            });
        }
    }
}
