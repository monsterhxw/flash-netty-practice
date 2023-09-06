package com.github.monsterhxw.ch13to15.client.handler;

import com.github.monsterhxw.ch13to15.proto.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch13to15.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msgRespPacket) throws Exception {
        System.out.println(getThreadName("Client") + "收到服务端的消息: " + msgRespPacket.getMessage());
    }
}
