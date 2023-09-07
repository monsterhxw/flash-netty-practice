package com.github.monsterhxw.ch17.client.handler;

import com.github.monsterhxw.ch17.protocol.response.LoginResponsePacket;
import com.github.monsterhxw.ch17.session.Session;
import com.github.monsterhxw.ch17.util.LoginUtil;
import com.github.monsterhxw.ch17.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch17.util.ThreadUtil.getThreadName;


/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        Integer userId = loginResponsePacket.getUserId();
        String username = loginResponsePacket.getUsername();

        if (loginResponsePacket.isSuccess()) {
            System.out.println(getThreadName("Client") + "登录成功: [userId: " + userId + ", username: " + username + "]");
            LoginUtil.markAsLogin(ctx.channel());
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());
        } else {
            System.out.println(getThreadName("Client") + "登录失败: userId: " + userId + ", username: " + username + ", 原因: " + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(getThreadName("Client") + "LoginResponseHandler#channelInactive(): channel" + ctx.channel() + "被关闭!");
    }
}
