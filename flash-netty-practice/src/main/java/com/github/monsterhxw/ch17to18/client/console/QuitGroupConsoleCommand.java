package com.github.monsterhxw.ch17to18.client.console;

import com.github.monsterhxw.ch17to18.protocol.request.QuitGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();

        System.out.print("请输入 groupId 退出群聊: ");
        String groupId = scanner.next();

        quitGroupRequestPacket.setGroupId(groupId);

        channel.writeAndFlush(quitGroupRequestPacket);
    }
}
