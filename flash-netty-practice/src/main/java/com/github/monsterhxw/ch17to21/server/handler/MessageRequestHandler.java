package com.github.monsterhxw.ch17to21.server.handler;

import com.github.monsterhxw.ch17to21.protocol.request.MessageRequestPacket;
import com.github.monsterhxw.ch17to21.protocol.response.MessageResponsePacket;
import com.github.monsterhxw.ch17to21.session.Session;
import com.github.monsterhxw.ch17to21.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch17to21.util.ThreadUtil.getThreadName;


/**
 * @author huangxuewei
 * @since 2023/9/6
 */
// 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static MessageRequestHandler getInstance() {
        return MessageRequestHandlerHolder.INSTANCE;
    }

    private static class MessageRequestHandlerHolder {
        private static final MessageRequestHandler INSTANCE = new MessageRequestHandler();
    }

    private MessageRequestHandler() {
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
