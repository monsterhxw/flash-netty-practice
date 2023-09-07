package com.github.monsterhxw.ch17to21.server.handler;

import com.github.monsterhxw.ch17to21.protocol.request.JoinGroupRequestPacket;
import com.github.monsterhxw.ch17to21.protocol.response.JoinGroupResponsePacket;
import com.github.monsterhxw.ch17to21.util.SessionUtil;
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
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    private JoinGroupRequestHandler() {

    }

    public static JoinGroupRequestHandler getInstance() {
        return JoinGroupRequestHandlerHolder.INSTANCE;
    }

    private static class JoinGroupRequestHandlerHolder {
        private static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) throws Exception {
        // 获取 Group 对应的 ChannelGroup，并将当前用户的 Channel 加入到该 Group 中
        String groupId = joinGroupRequestPacket.getGroupId();

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.add(ctx.channel());

        // 将响应结果发送给客户端
        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();
        joinGroupResponsePacket.setSuccess(true);
        joinGroupResponsePacket.setGroupId(groupId);

        ctx.channel().writeAndFlush(joinGroupResponsePacket);
    }
}
