package com.github.monsterhxw.ch08to09.proto;

import com.github.monsterhxw.ch08to09.seri.JsonSerializer;
import com.github.monsterhxw.ch08to09.seri.Serializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class PacketCodeCTest {

    private static final Serializer SERIALIZER;
    private static final Packet PACKET;

    static {
        SERIALIZER = new JsonSerializer();

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setVersion((byte) 1);
        loginRequestPacket.setUserId(110);
        loginRequestPacket.setUsername("huangxuewei");
        loginRequestPacket.setPassword("password123");
        PACKET = loginRequestPacket;
    }


    @Test
    public void encodeAndDecode() {
        ByteBuf encode = PacketCodeC.encode(PACKET);
        Packet decodePacket = PacketCodeC.decode(encode);

        byte[] srcBytes = SERIALIZER.serialize(PACKET);
        byte[] dstBytes = SERIALIZER.serialize(decodePacket);

        System.out.println("src bytes: " + Arrays.toString(srcBytes));
        System.out.println("dst bytes: " + Arrays.toString(dstBytes));

        Assert.assertArrayEquals(srcBytes, dstBytes);
    }
}