package com.github.monsterhxw.ch17to20.server.handler;

import com.github.monsterhxw.ch17to20.protocol.request.GroupMessageRequestPacket;
import com.github.monsterhxw.ch17to20.protocol.response.GroupMessageResponsePacket;
import com.github.monsterhxw.ch17to20.session.Session;
import com.github.monsterhxw.ch17to20.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
// 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    private static class GroupMessageRequestHandlerHolder {
        private static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();
    }

    public static GroupMessageRequestHandler getInstance() {
        return GroupMessageRequestHandlerHolder.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {
        // 根据 groupId 获取群组，发送群组消息
        String groupId = groupMessageRequestPacket.getToGroupId();
        String message = groupMessageRequestPacket.getMessage();

        // 根据 channel 获取 Session
        Session session = SessionUtil.getSession(ctx.channel());

        // 创建 GroupMessageResponsePacket 响应
        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();
        groupMessageResponsePacket.setFromGroupId(groupId);
        groupMessageResponsePacket.setMessage(message);
        groupMessageResponsePacket.setFromSession(session);

        // 根据 groupId 获取 ChannelGroup
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        // 发送群聊消息
        channelGroup.writeAndFlush(groupMessageResponsePacket);
    }
}
