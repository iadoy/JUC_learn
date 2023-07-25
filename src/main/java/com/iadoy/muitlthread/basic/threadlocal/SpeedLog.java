package com.iadoy.muitlthread.basic.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class SpeedLog {
    public static final ThreadLocal<Map<String, Long>> TIME_RECORD_LOCAL = ThreadLocal.withInitial(SpeedLog::initialStartTime);

    public static Map<String, Long> initialStartTime(){
        Map<String, Long> res = new HashMap<>();
        res.put("start", System.currentTimeMillis());
        res.put("last", System.currentTimeMillis());
        return res;
    }

    /**
     * 开始记录
     */
    public static final void beginSpeedLog(){
        log.info("start recording time.");
//        TIME_RECORD_LOCAL.get();
    }

    /**
     * 结束记录
     */
    public static final void endSpeedLog(){
        TIME_RECORD_LOCAL.remove();
        log.info("finish recording time.");
    }

    /**
     * 添加日志点
     */
    public static final void logPoint(String point){
        Long last = TIME_RECORD_LOCAL.get().get("last");
        long cost = System.currentTimeMillis() - last;
        TIME_RECORD_LOCAL.get().put(point + " cost:", cost);
        TIME_RECORD_LOCAL.get().put("last", System.currentTimeMillis());
    }

    /**
     * print方法的耗时
     */
    public static final void printCost() {

        Iterator<Map.Entry<String, Long>> it = TIME_RECORD_LOCAL.get().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            log.info(entry.getKey() + " =>" + entry.getValue());
        }
    }
}
