package com.github.monsterhxw.ch07;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Arrays;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);

        printByteBuf("allocate ByteBuf(9, 100)", buffer);

        // write 方法改变指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        printByteBuf("writeBytes(new byte[]{1, 2, 3, 4})", buffer);

        // write 方法改变指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        // 写完 int 类型的数据，写指针增加 4
        buffer.writeInt(10);
        printByteBuf("writeInt(10)", buffer);

        // write 方法改变指针，写完之后写指针等于 capacity 的时候，buffer 不可写
        buffer.writeBytes(new byte[]{5});
        printByteBuf("writeBytes(new byte[]{5})", buffer);

        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 也改变为 64
        buffer.writeBytes(new byte[]{6});
        printByteBuf("writeBytes(new byte[]{6})", buffer);

        // get 方法不改变读写指针
        int i = (buffer.readerIndex() + buffer.readableBytes()) - 1;
        System.out.println(buffer.getByte(i));
        printByteBuf("getByte(" + i + ")", buffer);
        System.out.println(buffer.getShort(1));
        printByteBuf("getShort(1)", buffer);
        System.out.println(buffer.getInt(1));
        printByteBuf("getInt(1)", buffer);

        // set 方法不改变读写指针
        i = buffer.readableBytes() - 1;
        System.out.println(buffer.getByte(i));
        buffer.setByte(i, 127);
        System.out.println(buffer.getByte(i));
        printByteBuf("setByte(" + i + ")" + ", 0) ", buffer);

        // read 方法改变指针
        int size = buffer.readableBytes();
        byte[] dst = new byte[size];
        System.out.println("readableBytes: " + buffer.readableBytes());
        buffer.readBytes(dst);
        System.out.println("readableBytes: " + buffer.readableBytes());
        System.out.println(Arrays.toString(dst));
        printByteBuf("readBytes(dst[" + size + "])", buffer);
    }

    private static void printByteBuf(String invoke, ByteBuf buffer) {
        System.out.println("after ==========" + invoke + "==========");
        System.out.println("    capacity()         : " + buffer.capacity());
        System.out.println("    maxCapacity()      : " + buffer.maxCapacity());
        System.out.println("    readerIndex()      : " + buffer.readerIndex());
        System.out.println("    readableBytes()    : " + buffer.readableBytes());
        System.out.println("    isReadable()       : " + buffer.isReadable());
        System.out.println("    writerIndex()      : " + buffer.writerIndex());
        System.out.println("    writableBytes()    : " + buffer.writableBytes());
        System.out.println("    isWritable()       : " + buffer.isWritable());
        System.out.println("    maxWritableBytes() : " + buffer.maxWritableBytes());
        System.out.println();
    }
}
