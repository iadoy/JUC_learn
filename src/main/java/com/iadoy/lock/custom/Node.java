package com.iadoy.lock.custom;

import lombok.Data;

@Data
public class Node {
    // 前一个节点，需要监听其locked字段
    Node pre;
    // true：当前线程正在抢占锁，或者已经占有锁
    // false：当前线程已经释放锁，下一个线程可以占有锁了
    volatile boolean locked;

    public Node(Node pre, boolean locked){
        this.pre = pre;
        this.locked = locked;
    }

    // 空节点
    public static final Node EMPTY = new Node(null, false);
}
