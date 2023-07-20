package com.iadoy.muitlthread.basic.create2;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程优先级实例代码
 */
@Slf4j
public class PriorityDemo {
    public static final int SLEEP_GAP = 1000;
    static class PrioritySetThread extends Thread{
        static int threadNo = 1;
        public PrioritySetThread(){
            super("thread-" + threadNo++);
        }

        public long opportunities = 0;

        @Override
        public void run() {
            while (true){
                opportunities++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //创建10个线程，并分别为其分配优先级1-10
          PrioritySetThread[] threads = new PrioritySetThread[10];
          for (int i = 0; i < 10; i++){
              PrioritySetThread thread = new PrioritySetThread();
              thread.setPriority(i + 1);
              threads[i] = thread;
          }

          //启动所有线程
          for (PrioritySetThread thread : threads){
              thread.start();
          }

          //等待1秒后结束所有线程
          Thread.sleep(SLEEP_GAP);
          for (PrioritySetThread thread : threads){
              thread.stop();
          }

          //输出
          for (PrioritySetThread thread : threads){
              log.info(thread.getName() + ": priority-{}, opportunities-{}", thread.getPriority(), thread.opportunities);
          }
    }
}
