package com.github.monsterhxw.ch08to10.client;

import com.github.monsterhxw.ch08to10.proto.*;
import com.github.monsterhxw.ch08to10.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.util.Random;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class ClientChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(getThreadName() + "channelActive-" + "client write login_request");

        // 编写 LoginRequestPacket
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(new Random().nextInt(Integer.MAX_VALUE));
        packet.setUsername("闪电侠");
        packet.setPassword("flash-man");

        // 获取 ByteBuf from ChannelHandlerContext
        ByteBuf byteBuf = ctx.alloc().ioBuffer();

        PacketCodeC.encode(byteBuf, packet);

        // 发送登录请求
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf respByteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.decode(respByteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.isSuccess()) {
                System.out.println(getThreadName() + "login success, " + loginResponsePacket);
                LoginUtil.markAsLogin(ctx.channel());
            } else {
                System.out.println(getThreadName() + "login failed, " + loginResponsePacket);
            }
        } else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket msgRespPacket = (MessageResponsePacket) packet;
            System.out.println(getThreadName() + "收到服务端的消息: " + msgRespPacket.getMessage());
        }

        respByteBuf.release();
    }

    private static String getThreadName() {
        return "Client-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }
}
