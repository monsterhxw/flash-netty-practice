package com.github.monsterhxw.ch17to21.client.handler;

import com.github.monsterhxw.ch17to21.protocol.request.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

import static com.github.monsterhxw.ch17to21.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    private static final int HEART_BEAT_INTERVAL = 5;
    private static final TimeUnit HEART_BEAT_INTERVAL_UNIT = TimeUnit.SECONDS;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        startHeartBeatTimer(ctx);
        super.channelActive(ctx);
    }

    private void startHeartBeatTimer(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                System.out.println(getThreadName("Client") + ctx.channel() + " 发送心跳包.........");
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                startHeartBeatTimer(ctx);
            }
        }, HEART_BEAT_INTERVAL, HEART_BEAT_INTERVAL_UNIT);
    }
}
