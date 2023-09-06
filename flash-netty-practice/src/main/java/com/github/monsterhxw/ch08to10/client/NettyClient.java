package com.github.monsterhxw.ch08to10.client;

import com.github.monsterhxw.ch08to10.proto.*;
import com.github.monsterhxw.ch08to10.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class NettyClient {

    private static final int PORT = 8080;

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName() + "init client socket: " + ch);
                        ch.pipeline().addLast(new ClientChannelInboundHandler());
                    }
                });

        connect(bootstrap, "127.0.0.1", PORT, MAX_RETRY);
    }

    private static void connect(final Bootstrap bootstrap, final String hostname, final int port, final int retry) {
        bootstrap
                .connect(hostname, port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        boolean success = future.isSuccess();
                        if (success) {
                            System.out.println(getThreadName() + " connect " + port + " success!");
                            Channel channel = ((ChannelFuture) future).channel();
                            startConsoleThread(bootstrap, channel);
                        } else {
                            int order = (MAX_RETRY - retry) + 1;
                            if (order <= MAX_RETRY) {
                                int delay = 1 << order;
                                System.out.println(getThreadName() + "connect " + port + " failed" + ", reconnect " + order + ", retry after " + delay + "s");
                                bootstrap.config()
                                        .group()
                                        .schedule((() -> connect(bootstrap, hostname, port, retry - 1)), delay, TimeUnit.SECONDS);
                            } else {
                                System.out.println(getThreadName() + "connect " + port + " failed, shutdown....");
                                bootstrap.group().shutdownGracefully();
                            }
                        }
                    }
                });
    }

    private static void startConsoleThread(final Bootstrap bootstrap, final Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.isLogin(channel)) {
                    System.out.println("请输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    // 构造 MessageRequestPacket 数据包
                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);

                    // 进行编码
                    ByteBuf byteBuf = channel.alloc().ioBuffer();
                    byteBuf = PacketCodeC.encode(byteBuf, packet);

                    // 发送消息
                    channel.writeAndFlush(byteBuf);

                    if (line.equals("exit")) {
                        bootstrap.group().shutdownGracefully();
                        break;
                    }
                }
            }
        }).start();
    }

    private static String getThreadName() {
        return "Client-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }
}
