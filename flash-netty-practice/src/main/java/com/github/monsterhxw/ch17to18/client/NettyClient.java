package com.github.monsterhxw.ch17to18.client;

import com.github.monsterhxw.ch17to18.client.handler.*;
import com.github.monsterhxw.ch17to18.client.console.ConsoleCommandManager;
import com.github.monsterhxw.ch17to18.client.console.LoginConsoleCommand;
import com.github.monsterhxw.ch17to18.parser.PacketDecoder;
import com.github.monsterhxw.ch17to18.parser.PacketEncoder;
import com.github.monsterhxw.ch17to18.parser.Splitter;
import com.github.monsterhxw.ch17to18.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.github.monsterhxw.ch17to18.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class NettyClient {

    private static final String HOSTNAME = "127.0.0.1";

    private static final int PORT = 8080;

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName("Client") + "init client socket: " + ch);
                        ch.pipeline()
                                .addLast(new Splitter())
                                .addLast(new PacketDecoder())
                                .addLast(new LoginResponseHandler())
                                .addLast(new LogoutResponseHandler(bootstrap.config().group()))
                                .addLast(new MessageResponseHandler())
                                .addLast(new CreateGroupResponseHandler())
                                .addLast(new JoinGroupResponseHandler())
                                .addLast(new QuitGroupResponseHandler())
                                .addLast(new ListGroupMembersResponseHandler())
                                .addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOSTNAME, PORT, MAX_RETRY);
    }

    private static void connect(final Bootstrap bootstrap, final String hostname, final int port, final int retry) {
        bootstrap
                .connect(hostname, port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        boolean success = future.isSuccess();
                        if (success) {
                            System.out.println(getThreadName("Client") + " connect " + port + " success!");
                            Channel channel = ((ChannelFuture) future).channel();
                            startConsoleThread(bootstrap, channel);
                        } else {
                            int order = (MAX_RETRY - retry) + 1;
                            if (order <= MAX_RETRY) {
                                int delay = 1 << order;
                                System.out.println(getThreadName("Client") + "connect " + port + " failed" + ", reconnect " + order + ", retry after " + delay + "s");
                                bootstrap.config()
                                        .group()
                                        .schedule((() -> connect(bootstrap, hostname, port, retry - 1)), delay, TimeUnit.SECONDS);
                            } else {
                                System.out.println(getThreadName("Client") + "connect " + port + " failed, shutdown....");
                                bootstrap.config().group().shutdownGracefully();
                            }
                        }
                    }
                });
    }

    private static void startConsoleThread(final Bootstrap bootstrap, final Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            while (!Thread.interrupted() && channel.isOpen()) {
                String command = null;
                if (SessionUtil.existsSession(channel)) {
                    command = consoleCommandManager.exec(scanner, channel);
                } else {
                    loginConsoleCommand.exec(scanner, channel);
                }
                if (command != null && command.equals("exit")) {
                    break;
                }
            }
            bootstrap.config().group().shutdownGracefully();
        }).start();
    }
}
