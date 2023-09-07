package com.github.monsterhxw.ch17to20.client.handler;

import com.github.monsterhxw.ch17to20.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws Exception {
        Integer fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        String message = messageResponsePacket.getMessage();
        System.out.println("收到来自【" + fromUserId + ", " + fromUserName + "】的消息:" + message);
    }
}
