package com.github.monsterhxw.ch13to16.server.handler;

import com.github.monsterhxw.ch13to16.proto.LoginRequestPacket;
import com.github.monsterhxw.ch13to16.proto.LoginResponsePacket;
import com.github.monsterhxw.ch13to16.session.Session;
import com.github.monsterhxw.ch13to16.util.LoginUtil;
import com.github.monsterhxw.ch13to16.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

import static com.github.monsterhxw.ch13to16.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        System.out.println(getThreadName("Server") + "收到客户端的登录请求: " + loginRequestPacket);

        // login validation
        boolean success = valid(loginRequestPacket);
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUsername(loginRequestPacket.getUsername());

        if (success) {
            System.out.println(getThreadName("Server") + "登录成功!");
            LoginUtil.markAsLogin(ctx.channel());

            // 绑定用户 Session
            Integer userId = new Random().nextInt(Integer.MAX_VALUE);
            String username = loginRequestPacket.getUsername();
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());

            loginResponsePacket.setSuccess(true);
            loginResponsePacket.setUserId(userId);
        } else {
            System.out.println(getThreadName("Server") + "登录失败!");
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason(success ? null : "账号密码校验失败");
        }

//        // write response info to client
//        ByteBuf respByteBuf = ctx.alloc().ioBuffer();
//        respByteBuf = PacketCodeC.encode(respByteBuf, loginResponsePacket);
//        ctx.channel().writeAndFlush(respByteBuf);

        // 不用手动获取 ByteBuf 并进行编码
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(getThreadName("Server") + "LoginRequestHandler#channelInactive(): channel" + ctx.channel() + "被关闭!");
        SessionUtil.unbindSession(ctx.channel());
    }

    private static boolean valid(LoginRequestPacket loginRequestPacket) {
        return loginRequestPacket.getUsername() != null && loginRequestPacket.getPassword() != null;
    }
}
