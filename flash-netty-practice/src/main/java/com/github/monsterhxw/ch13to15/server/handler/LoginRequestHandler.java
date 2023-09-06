package com.github.monsterhxw.ch13to15.server.handler;

import com.github.monsterhxw.ch13to15.proto.LoginRequestPacket;
import com.github.monsterhxw.ch13to15.proto.LoginResponsePacket;
import com.github.monsterhxw.ch13to15.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch13to15.util.ThreadUtil.getThreadName;

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
        if (success) {
            System.out.println(getThreadName("Server") + "登录成功!");
            loginResponsePacket.setSuccess(true);
            LoginUtil.markAsLogin(ctx.channel());
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

    private static boolean valid(LoginRequestPacket loginRequestPacket) {
        return loginRequestPacket.getUsername() != null && loginRequestPacket.getPassword() != null;
    }
}
