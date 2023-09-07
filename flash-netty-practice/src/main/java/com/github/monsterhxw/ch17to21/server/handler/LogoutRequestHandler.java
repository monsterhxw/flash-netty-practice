package com.github.monsterhxw.ch17to21.server.handler;

import com.github.monsterhxw.ch17to21.protocol.request.LogoutRequestPacket;
import com.github.monsterhxw.ch17to21.protocol.response.LogoutResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch17to21.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
// 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    private LogoutRequestHandler() {

    }

    public static LogoutRequestHandler getInstance() {
        return LogoutRequestHandlerHolder.INSTANCE;
    }

    private static class LogoutRequestHandlerHolder {
        private static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket msg) throws Exception {
        System.out.println(getThreadName("Server") + "收到客户端的退出登录请求: " + msg);

        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);

        ctx.channel().writeAndFlush(logoutResponsePacket);
    }
}
