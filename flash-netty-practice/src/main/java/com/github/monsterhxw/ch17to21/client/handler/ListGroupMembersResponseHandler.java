package com.github.monsterhxw.ch17to21.client.handler;

import com.github.monsterhxw.ch17to21.protocol.response.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket msg) throws Exception {
        System.out.println("群[" + msg.getGroupId() + "]成员列表: " + msg.getSessions());
    }
}
