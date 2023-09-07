package com.github.monsterhxw.ch17.server.handler;

import com.github.monsterhxw.ch17.protocol.request.MessageRequestPacket;
import com.github.monsterhxw.ch17.protocol.response.MessageResponsePacket;
import com.github.monsterhxw.ch17.session.Session;
import com.github.monsterhxw.ch17.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

import static com.github.monsterhxw.ch17.util.ThreadUtil.getThreadName;


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
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        // 获取 Session
        Session session = SessionUtil.getSession(ctx.channel());
        Integer fromUserId = session.getUserId();
        String fromUsername = session.getUsername();

        Integer toUserId = messageRequestPacket.getToUserId();
        String message = messageRequestPacket.getMessage();

        System.out.println(getThreadName("Server") + "收到发送消息请求: from[userId=" + fromUserId + "]" + ", to[userId=" + toUserId + "]" + ", message=" + message);

        // 构建响应
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(fromUserId);
        messageResponsePacket.setFromUserName(fromUsername);
        messageResponsePacket.setMessage(message);

        // 获取 toUserId 的 channel 进行发送消息
        Channel toUserChannel = SessionUtil.getChannel(toUserId);

        if (toUserChannel != null && toUserChannel.isActive() && SessionUtil.existsSession(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println(getThreadName("Server") + "[" + toUserId + "] 不在线，发送失败!");
        }
    }
}
