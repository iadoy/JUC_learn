package com.iadoy.lock;

import java.util.concurrent.locks.Lock;

public class IncrementData {
    public static int value = 0;

    public static void lockAndIncrement(Lock lock){
        lock.lock();
        try {
            value++;
        }finally {
            lock.unlock();
        }
    }
}
