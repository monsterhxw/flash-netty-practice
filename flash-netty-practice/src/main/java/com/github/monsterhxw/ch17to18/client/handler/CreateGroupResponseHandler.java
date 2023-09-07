package com.github.monsterhxw.ch17to18.client.handler;

import com.github.monsterhxw.ch17to18.protocol.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) throws Exception {
        String groupId = createGroupResponsePacket.getGroupId();
        List<String> userNameList = createGroupResponsePacket.getUserNameList();
        System.out.print("【创建群组成功】: 群组 ID: " + groupId + ", 群组成员列表: " + userNameList);
    }
}
