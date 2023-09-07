package com.github.monsterhxw.ch17to18.server.handler;

import com.github.monsterhxw.ch17to18.protocol.request.QuitGroupRequestPacket;
import com.github.monsterhxw.ch17to18.protocol.response.QuitGroupResponsePacket;
import com.github.monsterhxw.ch17to18.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket quitGroupRequestPacket) throws Exception {
        // 获取群对应的 ChannelGroup，并将当前用户的 Channel 从该 Group 中移除
        String groupId = quitGroupRequestPacket.getGroupId();

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.remove(ctx.channel());

        // 将响应结果发送给客户端
        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket();
        quitGroupResponsePacket.setSuccess(true);
        quitGroupResponsePacket.setGroupId(groupId);

        ctx.channel().writeAndFlush(quitGroupResponsePacket);
    }
}
