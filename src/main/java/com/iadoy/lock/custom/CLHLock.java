package com.iadoy.lock.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CLHLock implements Lock {
    //当前节点的线程本地变量
    private static ThreadLocal<Node> curNodeLocal = new ThreadLocal<>();
    //CLHLock队列的尾部指针，使用AtomicReference，方便进行CAS操作
    private AtomicReference<Node> tail = new AtomicReference<>(null);

    public CLHLock(){
        //设置尾部节点
        tail.getAndSet(Node.EMPTY);
    }

    //加锁操作：将节点添加到等待队列的尾部
    @Override
    public void lock() {
        Node cur = new Node(null, true);
        Node pre = tail.get();
        //CAS自旋：将当前节点插入队列的尾部
        while (!tail.compareAndSet(pre, cur)){
            pre = tail.get();
        }
        //设置前驱节点
        cur.setPre(pre);

        // 自旋，监听前驱节点的locked变量，直到其值为false
        // 若前驱节点的locked状态为true，则表示前一个线程还在抢占或者占有锁
        while (cur.getPre().isLocked()){
            //让出CPU时间片，提高性能
            Thread.yield();
        }

        // 能执行到这里，说明当前线程获取到了锁

        //将当前节点缓存在线程本地变量中，释放锁会用到
        curNodeLocal.set(cur);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        Node cur = curNodeLocal.get();
        cur.setLocked(false);
        cur.setPre(null); //help for GC
        curNodeLocal.set(null); //方便下一次抢锁
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
