package com.github.monsterhxw.ch17to21.parser;

import com.github.monsterhxw.ch17to21.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    private PacketCodecHandler() {

    }

    public static PacketCodecHandler getInstance() {
        return PacketCodecHandlerHolder.INSTANCE;
    }

    private static class PacketCodecHandlerHolder {
        private static final PacketCodecHandler INSTANCE = new PacketCodecHandler();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.encode(byteBuf, packet);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        out.add(PacketCodec.decode(byteBuf));
    }
}
