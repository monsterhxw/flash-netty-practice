package com.github.monsterhxw.ch17to19.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
