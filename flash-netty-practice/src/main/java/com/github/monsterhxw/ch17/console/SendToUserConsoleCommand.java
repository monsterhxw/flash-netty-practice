package com.github.monsterhxw.ch17.console;

import com.github.monsterhxw.ch17.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class SendToUserConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("【发送消息】请输入用户 ID: ");
        String toUserId = scanner.next();
        System.out.print("【发送消息】请输入消息: ");
        String message = scanner.next();

        // 构造 MessageRequestPacket 数据包
        MessageRequestPacket messageRequestPacket = new MessageRequestPacket(Integer.valueOf(toUserId), message);

        // 发送消息
        channel.writeAndFlush(messageRequestPacket);
    }
}
