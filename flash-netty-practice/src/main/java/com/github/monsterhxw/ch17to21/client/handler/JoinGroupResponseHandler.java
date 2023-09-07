package com.github.monsterhxw.ch17to21.client.handler;

import com.github.monsterhxw.ch17to21.protocol.response.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket joinGroupResponsePacket) throws Exception {
        if (joinGroupResponsePacket.isSuccess()) {
            System.out.println("加入群组[" + joinGroupResponsePacket.getGroupId() + "]成功!");
        } else {
            System.out.println("加入群组[" + joinGroupResponsePacket.getGroupId() + "]失败，原因为: " + joinGroupResponsePacket.getReason());
        }
    }
}
