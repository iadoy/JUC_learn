package com.iadoy.muitlthread.basic.threadlocal;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Foo {
    static final AtomicInteger AMOUNT = new AtomicInteger(0); //实例总数
    int index;  //对象的编号
    int bar = 10;   //对象的内容

    public Foo(){
        index = AMOUNT.incrementAndGet();
    }
}
