package com.github.monsterhxw.ch17to21.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class IMIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;
    private static final TimeUnit READER_IDLE_TIME_UNIT = TimeUnit.SECONDS;

    public IMIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, READER_IDLE_TIME_UNIT);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READER_IDLE_TIME + "s 未读到数据，关闭连接[" + ctx.channel() + "!");
        ctx.channel().close();
    }
}
