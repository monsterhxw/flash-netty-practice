package com.github.monsterhxw.ch13to16.server.handler;

import com.github.monsterhxw.ch13to16.proto.MessageRequestPacket;
import com.github.monsterhxw.ch13to16.proto.MessageResponsePacket;
import com.github.monsterhxw.ch13to16.session.Session;
import com.github.monsterhxw.ch13to16.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

import static com.github.monsterhxw.ch13to16.util.ThreadUtil.getThreadName;

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
//
//        if (msgReqPacket.getMessage().equals("exit")) {
//            ctx.channel().close();
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            return;
//        }
//
//        // 响应
//        MessageResponsePacket msgRespPacket = new MessageResponsePacket();
//        msgRespPacket.setMessage("服务端 Echo: 【" + msgReqPacket.getMessage() + "】");

//        // 进行编码
//        ByteBuf respByteBuf = ctx.alloc().ioBuffer();
//        respByteBuf = PacketCodeC.encode(respByteBuf, msgRespPacket);

//        // 发送消息
//        ctx.channel().writeAndFlush(msgRespPacket);

        // 获取 Session
        Session session = SessionUtil.getSession(ctx.channel());

        // 构建响应
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUsername());
        messageResponsePacket.setMessage(msgReqPacket.getMessage());

        // 获取 toUserId 的 channel 进行发送消息
        Channel toUserChannel = SessionUtil.getChannel(msgReqPacket.getToUserId());
        if (toUserChannel != null && toUserChannel.isActive() && SessionUtil.existsSession(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println(getThreadName("Server") + "[" + msgReqPacket.getToUserId() + "] 不在线，发送失败!");
            MessageResponsePacket msgRespPacket = new MessageResponsePacket();
            msgRespPacket.setMessage("[" + msgReqPacket.getToUserId() + "] 不在线!");
            ctx.channel().writeAndFlush(msgRespPacket);
        }
    }
}
