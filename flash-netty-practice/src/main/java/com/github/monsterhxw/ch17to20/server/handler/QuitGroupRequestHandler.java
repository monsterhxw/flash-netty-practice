package com.github.monsterhxw.ch17to20.server.handler;

import com.github.monsterhxw.ch17to20.protocol.request.QuitGroupRequestPacket;
import com.github.monsterhxw.ch17to20.protocol.response.QuitGroupResponsePacket;
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
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    private QuitGroupRequestHandler() {

    }

    public static QuitGroupRequestHandler getInstance() {
        return QuitGroupRequestHandlerHolder.INSTANCE;
    }

    private static class QuitGroupRequestHandlerHolder {
        private static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();
    }

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
