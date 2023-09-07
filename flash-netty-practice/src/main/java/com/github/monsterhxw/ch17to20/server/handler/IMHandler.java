package com.github.monsterhxw.ch17to20.server.handler;

import com.github.monsterhxw.ch17to20.protocol.Packet;
import com.github.monsterhxw.ch17to20.protocol.command.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {

    private final Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler() {
        handlerMap = new HashMap<>();

        handlerMap.put(Command.MESSAGE_REQUEST, MessageRequestHandler.getInstance());

        handlerMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestHandler.getInstance());

        handlerMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.getInstance());

        handlerMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.getInstance());

        handlerMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.getInstance());

        handlerMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.getInstance());

        handlerMap.put(Command.LOGOUT_REQUEST, LogoutRequestHandler.getInstance());
    }

    public static IMHandler getInstance() {
        return IMHandlerHolder.INSTANCE;
    }

    private static class IMHandlerHolder {
        private static final IMHandler INSTANCE = new IMHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        handlerMap.get(msg.getCommand()).channelRead(ctx, msg);
    }
}
