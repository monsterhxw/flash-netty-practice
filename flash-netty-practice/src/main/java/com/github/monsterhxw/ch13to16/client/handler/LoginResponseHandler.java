package com.github.monsterhxw.ch13to16.client.handler;

import com.github.monsterhxw.ch13to16.proto.LoginResponsePacket;
import com.github.monsterhxw.ch13to16.session.Session;
import com.github.monsterhxw.ch13to16.util.LoginUtil;
import com.github.monsterhxw.ch13to16.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch13to16.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(getThreadName("Client") + "channelActive-" + "client write login_request");
//        // 编写 LoginRequestPacket
//        LoginRequestPacket packet = new LoginRequestPacket();
//        packet.setUserId(new Random().nextInt(Integer.MAX_VALUE));
//        packet.setUsername("闪电侠");
//        packet.setPassword("flash-man");
//
////        // 获取 ByteBuf from ChannelHandlerContext
////        ByteBuf byteBuf = ctx.alloc().ioBuffer();
////
////        PacketCodeC.encode(byteBuf, packet);
//
//        // 发送登录请求
//        // 不用手动获取 ByteBuf 并进
//        ctx.channel().writeAndFlush(packet);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        Integer userId = loginResponsePacket.getUserId();
        String username = loginResponsePacket.getUsername();

        if (loginResponsePacket.isSuccess()) {
            System.out.println(getThreadName("Client") + "登录成功, userId: " + userId + ", username: " + username);
            LoginUtil.markAsLogin(ctx.channel());
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());
        } else {
            System.out.println(getThreadName("Client") + "登录失败, userId: " + userId + ", username: " + username + ", 原因: " + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(getThreadName("Client") + "LoginResponseHandler#channelInactive(): channel" + ctx.channel() + "被关闭!");
        SessionUtil.unbindSession(ctx.channel());
    }
}
