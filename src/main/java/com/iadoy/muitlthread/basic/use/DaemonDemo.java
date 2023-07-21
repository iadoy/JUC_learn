package com.iadoy.muitlthread.basic.use;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 守护线程示例代码
 */
@Slf4j
public class DaemonDemo {
    public static final int SLEEP_GAP = 500;
    public static final int MAX_TURN = 4;

    static class DaemonThread extends Thread{
        public DaemonThread(){
            super("daemonThread");
            setDaemon(true);
        }

        @SneakyThrows
        @Override
        public void run() {
            log.info("start daemon thread");
            int i = 1;
            while (true){
                log.info("--turns: {}, daemon: {}", i, isDaemon());
                i++;
                sleep(SLEEP_GAP);
            }
        }
    }

    public static void main(String[] args) {
        //创建一个无法主动结束的守护线程
        Thread dThread = new DaemonThread();
        dThread.start();

        //创建一个会执行结束的用户线程
        Thread userThread = new Thread(() -> {
            log.info("start user thread");
            for (int i = 0; i < MAX_TURN; i++){
                log.info(">>turns: {}, daemon: {}", i, Thread.currentThread().isDaemon());
                try {
                    Thread.sleep(SLEEP_GAP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("user thread is FINISHED.");
        }, "userThread");
        userThread.start();

        //输出一下main线程是否是守护线程
        log.info("daemon: {}", Thread.currentThread().isDaemon());
        log.info("FINISHED!!!");
    }
}
