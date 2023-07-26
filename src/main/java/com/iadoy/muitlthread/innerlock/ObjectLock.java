package com.iadoy.muitlthread.innerlock;

import com.iadoy.muitlthread.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 本类中获取哈希值、线程id等方法均需要将大端模式的数据转换成小端模式
 * <p>因为java默认的是大端，而分析工具 JOL 需要小端
 */
@Slf4j
public class ObjectLock {
    private Integer amount = 0; //整形字段占用4字节

    public void increase(){
        synchronized (this){
            amount++;
        }
    }

    /**
     * 返回十六进制、小端模式的hashCode
     */
    public String hexHash(){
        //获取对象的原始哈希值，Java默认是大端
        int haseCode = this.hashCode();

        //转换成小端模式的字节数组
        byte[] haseCode_LE = ByteUtil.int2Bytes_LE(haseCode);

        //转换成十六进制字符串
        return ByteUtil.byteToHex(haseCode_LE);
    }

    /**
     * 输出二进制、小端模式的hashCode
     */
    public String binaryHash(){
        //获取对象的原始哈希值，Java默认是大端
        int haseCode = this.hashCode();

        //转换成小端模式的字节数组
        byte[] haseCode_LE = ByteUtil.int2Bytes_LE(haseCode);

        StringBuilder builder = new StringBuilder();
        for (byte b : haseCode_LE){
            //转成二进制形式的字符串
            builder.append(ByteUtil.byte2BinaryString(b));
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * 返回十六进制、小端模式的ThreadId
     */
    public String hexThreadId(){
        long id = Thread.currentThread().getId();
        byte[] id_LE = ByteUtil.long2bytes_LE(id);
        return ByteUtil.byteToHex(id_LE);
    }

    /**
     * 输出二进制、小端模式的ThreadId
     */
    public String binaryThreadId(){
        long id = Thread.currentThread().getId();
        byte[] id_LE = ByteUtil.long2bytes_LE(id);

        StringBuilder builder = new StringBuilder();
        for (byte b : id_LE){
            //转成二进制形式的字符串
            builder.append(ByteUtil.byte2BinaryString(b));
            builder.append(" ");
        }
        return builder.toString();
    }

    public void printSelf(){
        log.info("lock hash: {}", hexHash());
        log.info("lock binaryHash: {}", binaryHash());
        String printTable = ClassLayout.parseInstance(this).toPrintable();
        log.info("lock: {}", printTable);
    }
}
