package com.github.monsterhxw.ch17.server.handler;

import com.github.monsterhxw.ch17.protocol.request.LogoutRequestPacket;
import com.github.monsterhxw.ch17.protocol.response.LogoutResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch17.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket msg) throws Exception {
        System.out.println(getThreadName("Server") + "收到客户端的退出登录请求: " + msg);

        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);

        ctx.channel().writeAndFlush(logoutResponsePacket);
    }
}
