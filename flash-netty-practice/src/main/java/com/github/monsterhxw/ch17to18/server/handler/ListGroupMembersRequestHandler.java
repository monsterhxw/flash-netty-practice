package com.github.monsterhxw.ch17to18.server.handler;

import com.github.monsterhxw.ch17to18.protocol.request.ListGroupMembersRequestPacket;
import com.github.monsterhxw.ch17to18.protocol.response.ListGroupMembersResponsePacket;
import com.github.monsterhxw.ch17to18.session.Session;
import com.github.monsterhxw.ch17to18.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket listGroupMembersRequestPacket) throws Exception {
        // 获取 group 的 ChannelGroup
        String groupId = listGroupMembersRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        // 遍历群成员的 channel，对应的 session，构建群成员信息
        List<Session> sessions = new ArrayList<>();
        for (Channel channel : channelGroup) {
            sessions.add(SessionUtil.getSession(channel));
        }

        // 构建响应信息
        ListGroupMembersResponsePacket listGroupMembersResponsePacket = new ListGroupMembersResponsePacket();
        listGroupMembersResponsePacket.setGroupId(groupId);
        listGroupMembersResponsePacket.setSessions(sessions);

        // 发送响应信息
        ctx.channel().writeAndFlush(listGroupMembersResponsePacket);
    }
}
