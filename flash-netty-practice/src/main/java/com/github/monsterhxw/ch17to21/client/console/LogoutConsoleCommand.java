package com.github.monsterhxw.ch17to21.client.console;

import com.github.monsterhxw.ch17to21.protocol.request.LogoutRequestPacket;
import com.github.monsterhxw.ch17to21.session.Session;
import com.github.monsterhxw.ch17to21.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        Session session;
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        if ((session = SessionUtil.getSession(channel)) != null) {
            logoutRequestPacket.setUserId(session.getUserId());
            logoutRequestPacket.setUsername(session.getUsername());
        }

        // 发送消息
        channel.writeAndFlush(logoutRequestPacket);
    }
}
