package com.iadoy.muitlthread.basic.use;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 【示例代码】守护线程创建的线程也是守护线程
 */
@Slf4j
public class DaemonDemo2 {
    public static final int SLEEP_GAP = 500;
    public static final int MAX_TURN = 4;

    static class NormalThread extends Thread{
        static int threadNo = 1;
        public NormalThread(){
            super("normalThread-" + threadNo++);
        }

        @SneakyThrows
        @Override
        public void run() {
            int i = 0;
            while (true){
                sleep(SLEEP_GAP);
                log.info(getName() + " daemon: {}", isDaemon());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread dThread = new Thread(() -> {
            for (int i = 0; i < MAX_TURN; i++){
                Thread normalThread = new NormalThread();
                //在这里手动将线程设置为用户线程，则该线程会转变为用户线程
//                normalThread.setDaemon(false);
                normalThread.start();
            }
        }, "daemonThread");
        dThread.setDaemon(true);
        dThread.start();

        //保留一下唯一的用户线程main，否则立即退出了，看不到日志输出
        //守护线程中将创建的线程设置为用户线程，则可以不需要保住main线程
        Thread.sleep(SLEEP_GAP);
        log.info(Thread.currentThread().getName() + " is finished.");
    }
}
