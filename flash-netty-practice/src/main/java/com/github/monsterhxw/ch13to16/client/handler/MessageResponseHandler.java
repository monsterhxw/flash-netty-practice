package com.github.monsterhxw.ch13to16.client.handler;

import com.github.monsterhxw.ch13to16.proto.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch13to16.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws Exception {
        System.out.println(getThreadName("Client") + "MessageResponseHandle#channelRead0");
        Integer fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        String message = messageResponsePacket.getMessage();
        System.out.println(getThreadName("Client") + "收到来自【" + fromUserId + ", " + fromUserName + "】的消息:" + message);
    }
}
