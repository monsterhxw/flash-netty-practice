package com.github.monsterhxw.ch17to19.client.console;

import com.github.monsterhxw.ch17to19.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.util.Optional.ofNullable;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class ConsoleCommandManager {

    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("sendToGroup", new SendToGroupConsoleCommand());
    }

    public String exec(Scanner scanner, Channel channel) {
        // 获取第一个指令
        System.out.printf("请输入指令[%s]: ", consoleCommandMap.keySet());
        String command = scanner.next();
        if (command.equals("exit") || !SessionUtil.existsSession(channel)) {
            return "exit";
        }

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        ofNullable(consoleCommand)
                .ifPresentOrElse(cmd -> cmd.exec(scanner, channel),
                        () -> System.out.println("无法识别[" + command + "]指令，请重新输入!"));

        System.out.println();

        return command;
    }
}
