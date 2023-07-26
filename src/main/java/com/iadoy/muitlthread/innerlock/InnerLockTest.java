package com.iadoy.muitlthread.innerlock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.vm.VM;

/**
 * 使用 JOL 打印空对象的结构
 */
@Slf4j
public class InnerLockTest {
    @Test
    public void testNoLockObject(){
        log.info(VM.current().details());

        ObjectLock objectLock = new ObjectLock();
        log.info("print object");
        objectLock.printSelf();
    }
}
