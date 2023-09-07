package com.github.monsterhxw.ch17to19.client.console;

import com.github.monsterhxw.ch17to19.protocol.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        System.out.print("【登录】请输入用户名:");
        String username = scanner.next();

        loginRequestPacket.setUsername(username);
        loginRequestPacket.setPassword("password-" + username);

        // 发送消息
        channel.writeAndFlush(loginRequestPacket);

        waitForLoginResponse();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException ignored) {
        }
    }
}
