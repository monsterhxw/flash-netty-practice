package com.github.monsterhxw.ch17to19.client.handler;

import com.github.monsterhxw.ch17to19.protocol.response.GroupMessageResponsePacket;
import com.github.monsterhxw.ch17to19.session.Session;
import com.github.monsterhxw.ch17to19.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket groupMessageResponsePacket) throws Exception {
        String fromGroupId = groupMessageResponsePacket.getFromGroupId();
        String message = groupMessageResponsePacket.getMessage();
        Session fromSession = groupMessageResponsePacket.getFromSession();

        Session session;
        if ((session = SessionUtil.getSession(ctx.channel())) != null && !session.getUserId().equals(fromSession.getUserId())) {
            System.out.println("收到群[" + fromGroupId + "]中[userId=" + fromSession.getUserId() + ",userName=" + fromSession.getUsername() + "]发来的消息:" + message);
        }
    }
}
