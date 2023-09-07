package com.github.monsterhxw.ch17to20.client.console;

import com.github.monsterhxw.ch17to20.protocol.request.CreateGroupRequestPacket;
import com.github.monsterhxw.ch17to20.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITTER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
        System.out.print("【创建群组】请输入用户 ID 列表，用英文逗号分隔: ");
        String userIdStr = scanner.next();

        List<Integer> userIds = new ArrayList<>();
        ofNullable(SessionUtil.getSession(channel)).ifPresent(session -> userIds.add(session.getUserId()));
        userIds.addAll(Arrays.stream(userIdStr.split(USER_ID_SPLITTER))
                .map(Integer::valueOf)
                .collect(Collectors.toList()));

        createGroupRequestPacket.setUserIdList(userIds);

        // 发送消息
        channel.writeAndFlush(createGroupRequestPacket);
    }
}
