package com.github.monsterhxw.ch17to21.server.handler;

import com.github.monsterhxw.ch17to21.protocol.request.HeartBeatRequestPacket;
import com.github.monsterhxw.ch17to21.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    private HeartBeatRequestHandler() {
    }

    public static HeartBeatRequestHandler getInstance() {
        return HeartBeatRequestHandlerHolder.INSTANCE;
    }

    private static class HeartBeatRequestHandlerHolder {
        private static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket msg) throws Exception {
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
