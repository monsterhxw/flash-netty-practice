package com.github.monsterhxw.ch13to16.client;

import com.github.monsterhxw.ch13to16.client.handler.LoginResponseHandler;
import com.github.monsterhxw.ch13to16.client.handler.MessageResponseHandler;
import com.github.monsterhxw.ch13to16.proto.*;
import com.github.monsterhxw.ch13to16.util.LoginUtil;
import com.github.monsterhxw.ch13to16.util.SessionUtil;
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

import static com.github.monsterhxw.ch13to16.util.ThreadUtil.getThreadName;

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
                                .addLast(new MessageResponseHandler())
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
                                bootstrap.group().shutdownGracefully();
                            }
                        }
                    }
                });
    }

    private static void startConsoleThread(final Bootstrap bootstrap, final Channel channel) {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
            MessageRequestPacket messageRequestPacket = new MessageRequestPacket();

            while (!Thread.interrupted() && channel.isActive()) {
//                if (LoginUtil.isLogin(channel)) {
//                    System.out.println("请输入消息发送至服务端: ");
//                    Scanner sc = new Scanner(System.in);
//                    String line = sc.nextLine();
//
//                    // 构造 MessageRequestPacket 数据包
//                    MessageRequestPacket packet = new MessageRequestPacket();
//                    packet.setMessage(line);
//
//                    // 进行编码
////                    ByteBuf byteBuf = channel.alloc().ioBuffer();
////                    byteBuf = PacketCodeC.encode(byteBuf, packet);
////
////                    // 发送消息
////                    channel.writeAndFlush(byteBuf);
//                    channel.writeAndFlush(packet);
//
//                    if (line.equals("exit")) {
//                        break;
//                    }
//                }
                if (LoginUtil.isLogin(channel) && SessionUtil.existsSession(channel)) {
                    System.out.print("请输入你要发送消息的用户 ID: ");
                    Integer toUserId = Integer.valueOf(sc.nextLine());
                    System.out.print("请输入你要发送的信息: ");
                    String message = sc.nextLine();

                    messageRequestPacket.setToUserId(toUserId);
                    messageRequestPacket.setMessage(message);

                    // 发送数据
                    channel.writeAndFlush(messageRequestPacket);
                } else {
                    System.out.print("请输入用户名进行登录: ");
                    String username = sc.nextLine();
                    System.out.print("请输入密码: ");
                    String password = sc.nextLine();

                    loginRequestPacket.setUsername(username);
                    loginRequestPacket.setPassword(password);

                    // 发送数据
                    channel.writeAndFlush(loginRequestPacket);

                    waitForLoginResponse();
                }
            }
            bootstrap.group().shutdownGracefully();
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException ignored) {
        }
    }
}
