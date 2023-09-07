package com.github.monsterhxw.ch17to20.server.handler;

import com.github.monsterhxw.ch17to20.protocol.request.CreateGroupRequestPacket;
import com.github.monsterhxw.ch17to20.protocol.response.CreateGroupResponsePacket;
import com.github.monsterhxw.ch17to20.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.monsterhxw.ch17to20.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
// 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    private static final Random RANDOM = new Random();

    private CreateGroupRequestHandler() {

    }

    public static CreateGroupRequestHandler getInstance() {
        return CreateGroupRequestHandlerHolder.INSTANCE;
    }

    private static class CreateGroupRequestHandlerHolder {
        private static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
        System.out.println(getThreadName("Server") + "收到客户端的创建群组请求: " + createGroupRequestPacket);

        List<Integer> userIds = createGroupRequestPacket.getUserIdList();
        List<String> userNames = new ArrayList<>();

        // 创建一个 Netty ChannelGroup
        DefaultChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 筛选出用户加入到 Netty ChannelGroup 中
        Channel channel;
        for (Integer userId : userIds) {
            if ((channel = SessionUtil.getChannel(userId)) != null) {
                channelGroup.add(channel);
                userNames.add(SessionUtil.getSession(channel).getUsername());
            }
        }

        String groupId = String.valueOf(System.currentTimeMillis());
        // 创建响应结果
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(groupId);
        createGroupResponsePacket.setUserNameList(userNames);

        // 将响应结果发送给客户端
        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.println(getThreadName("Server") + "创建群组成功,群组 ID: " + groupId + ", 群组成员: " + userNames);

        // 保存群组信息
        SessionUtil.bindChannelGroup(groupId, channelGroup);
    }
}
