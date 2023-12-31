package com.github.monsterhxw.ch17to21.parser;

import com.github.monsterhxw.ch17to21.protocol.command.Command;
import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.request.*;
import com.github.monsterhxw.ch17to21.protocol.response.*;
import com.github.monsterhxw.ch17to21.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class PacketCodec {

    static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte, Class<? extends Packet>> PACKET_TYPE_MAP;

    private static final Map<Byte, Serializer> SERIALIZER_MAP;

    static {
        PACKET_TYPE_MAP = new HashMap<>();
        PACKET_TYPE_MAP.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);

        PACKET_TYPE_MAP.put(Command.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);

        SERIALIZER_MAP = new HashMap<>();
        SERIALIZER_MAP.put(Serializer.DEFAULT.getSerializerAlgorithm(), Serializer.DEFAULT);
    }

    public static ByteBuf encode(Packet packet) {
        return encode(null, packet);
    }

    public static ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        if (byteBuf == null) {
            // 创建 ByteBuf 对象
            byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        }

        // 封装协议
        return encapsulateProto(byteBuf, packet);
    }

    public static Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过 version
        byteBuf.skipBytes(1);

        // 读 1-byte 序列化算法
        byte serializerAlgorithm = byteBuf.readByte();

        // 读 1-byte 指令
        byte command = byteBuf.readByte();

        // 读 4-bytes 数据长度
        int length = byteBuf.readInt();

        // 读数据
        byte[] data = new byte[length];
        byteBuf.readBytes(data);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);

        if (requestType == null || serializer == null) {
            throw new UnsupportedOperationException("不支持的指令类型");
        }

        return serializer.deserialize(requestType, data);
    }

    /**
     * 1. magic_number (4-byte)
     * 2. packet_version (1-byte)
     * 3. serialize_algorithm (1-byte)
     * 4. packet_command (1-byte)
     * 5. data_length (4-bytes)
     * 6. data (N-bytes)
     *
     * @param byteBuf
     * @param packet
     * @return
     */
    private static ByteBuf encapsulateProto(ByteBuf byteBuf, Packet packet) {
        // 写入 magic number
        byteBuf.writeInt(MAGIC_NUMBER);

        // 写 packet version
        byteBuf.writeByte(packet.getVersion());

        // 写序列化算法
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());

        // 写 packet command
        byteBuf.writeByte(packet.getCommand());

        // 序列化 packet
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        // 写数据长度
        byteBuf.writeInt(bytes.length);
        // 写数据
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    private static Class<? extends Packet> getRequestType(byte command) {
        return PACKET_TYPE_MAP.get(command);
    }

    private static Serializer getSerializer(byte serializerAlgorithm) {
        return SERIALIZER_MAP.get(serializerAlgorithm);
    }
}
