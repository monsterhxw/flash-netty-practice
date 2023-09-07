package com.github.monsterhxw.ch17to18.client.console;

import com.github.monsterhxw.ch17to18.protocol.request.ListGroupMembersRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();

        System.out.print("请输入 groupId 查询群成员列表: ");
        String groupId = scanner.next();

        listGroupMembersRequestPacket.setGroupId(groupId);

        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
