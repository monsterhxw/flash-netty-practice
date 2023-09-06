package com.github.monsterhxw.ch12.client.handler;

import com.github.monsterhxw.ch12.proto.LoginRequestPacket;
import com.github.monsterhxw.ch12.proto.LoginResponsePacket;
import com.github.monsterhxw.ch12.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

import static com.github.monsterhxw.ch12.util.ThreadUtil.*;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(getThreadName("Client") + "channelActive-" + "client write login_request");
        // 编写 LoginRequestPacket
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(new Random().nextInt(Integer.MAX_VALUE));
        packet.setUsername("闪电侠");
        packet.setPassword("flash-man");

//        // 获取 ByteBuf from ChannelHandlerContext
//        ByteBuf byteBuf = ctx.alloc().ioBuffer();
//
//        PacketCodeC.encode(byteBuf, packet);

        // 发送登录请求
        // 不用手动获取 ByteBuf 并进
        ctx.channel().writeAndFlush(packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.isSuccess()) {
            System.out.println(getThreadName("Client") + "login success, " + loginResponsePacket);
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(getThreadName("Client") + "login failed, " + loginResponsePacket);
        }
    }
}
