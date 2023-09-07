package com.github.monsterhxw.ch17to21.client.handler;

import com.github.monsterhxw.ch17to21.protocol.response.QuitGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket quitGroupResponsePacket) throws Exception {
        if (quitGroupResponsePacket.isSuccess()) {
            System.out.println("退出群组[" + quitGroupResponsePacket.getGroupId() + "]成功!");
        } else {
            System.out.println("退出群组[" + quitGroupResponsePacket.getGroupId() + "]失败，原因为: " + quitGroupResponsePacket.getReason());
        }
    }
}
