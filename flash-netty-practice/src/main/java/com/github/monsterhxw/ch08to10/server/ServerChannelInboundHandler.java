package com.github.monsterhxw.ch08to10.server;

import com.github.monsterhxw.ch08to10.proto.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class ServerChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;

    public ServerChannelInboundHandler(final NioEventLoopGroup bossGroup, final NioEventLoopGroup workerGroup) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf reqByteBuf = (ByteBuf) msg;
        // decode
        Packet packet = PacketCodeC.decode(reqByteBuf);
        // 判断是否为 login 请求
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            // login validation
            boolean success = valid(loginRequestPacket);
            if (success) {
                System.out.println(getThreadName() + "login success, " + loginRequestPacket);
            } else {
                System.out.println(getThreadName() + "login failed, " + loginRequestPacket);
            }

            // login response
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setSuccess(success);
            loginResponsePacket.setReason(success ? null : "账号密码校验失败");

            // write response info to client
            ByteBuf respByteBuf = ctx.alloc().ioBuffer();
            respByteBuf = PacketCodeC.encode(respByteBuf, loginResponsePacket);
            ctx.channel().writeAndFlush(respByteBuf);

        } else if (packet instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket msgReqPacket = (MessageRequestPacket) packet;
            System.out.println(getThreadName() + "收到客户端消息: " + msgReqPacket.getMessage());

            if (msgReqPacket.getMessage().equals("exit")) {
                ctx.channel().close();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                reqByteBuf.release();
                return;
            }

            // 响应
            MessageResponsePacket msgRespPacket = new MessageResponsePacket();
            msgRespPacket.setMessage("服务端 Echo: 【" + msgReqPacket.getMessage() + "】");

            // 进行编码
            ByteBuf respByteBuf = ctx.alloc().ioBuffer();
            respByteBuf = PacketCodeC.encode(respByteBuf, msgRespPacket);

            // 发送消息
            ctx.channel().writeAndFlush(respByteBuf);
        }

        reqByteBuf.release();
    }

    private static boolean valid(LoginRequestPacket loginRequestPacket) {
        return loginRequestPacket.getUsername() != null && loginRequestPacket.getPassword() != null;
    }

    private static String getThreadName() {
        return "Server-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }
}
