package com.github.monsterhxw.ch17to18.client.console;

import com.github.monsterhxw.ch17to18.protocol.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();
        System.out.print("请输入 groupId，加入群聊: ");

        String groupId = scanner.next();

        joinGroupRequestPacket.setGroupId(groupId);

        channel.writeAndFlush(joinGroupRequestPacket);
    }
}
