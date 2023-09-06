package com.github.monsterhxw.ch13.server.handler;

import com.github.monsterhxw.ch13.proto.MessageRequestPacket;
import com.github.monsterhxw.ch13.proto.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

import static com.github.monsterhxw.ch13.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;

    public MessageRequestHandler(final NioEventLoopGroup bossGroup, final NioEventLoopGroup workerGroup) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msgReqPacket) throws Exception {
        System.out.println(getThreadName("Server") + "收到客户端消息: " + msgReqPacket.getMessage());

        if (msgReqPacket.getMessage().equals("exit")) {
            ctx.channel().close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            return;
        }

        // 响应
        MessageResponsePacket msgRespPacket = new MessageResponsePacket();
        msgRespPacket.setMessage("服务端 Echo: 【" + msgReqPacket.getMessage() + "】");

//        // 进行编码
//        ByteBuf respByteBuf = ctx.alloc().ioBuffer();
//        respByteBuf = PacketCodeC.encode(respByteBuf, msgRespPacket);

        // 发送消息
        ctx.channel().writeAndFlush(msgRespPacket);
    }
}
