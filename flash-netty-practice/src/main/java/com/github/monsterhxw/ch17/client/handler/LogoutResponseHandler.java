package com.github.monsterhxw.ch17.client.handler;

import com.github.monsterhxw.ch17.protocol.response.LogoutResponsePacket;
import com.github.monsterhxw.ch17.session.Session;
import com.github.monsterhxw.ch17.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    private final EventLoopGroup group;

    public LogoutResponseHandler(EventLoopGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket msg) throws Exception {
        Session oldSession = SessionUtil.getSession(ctx.channel());

        SessionUtil.unbindSession(ctx.channel());

        System.out.println("[userId: " + oldSession.getUserId() + ", username: " + oldSession.getUsername() + "]: 已退出登录");

        this.group.shutdownGracefully();
    }
}
