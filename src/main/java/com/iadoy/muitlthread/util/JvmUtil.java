package com.iadoy.muitlthread.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class JvmUtil {

    /**
     * 通过反射获取Unsafe实例
     */
    public static Unsafe getUnsafe() {
        Unsafe res = null;
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            res = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
