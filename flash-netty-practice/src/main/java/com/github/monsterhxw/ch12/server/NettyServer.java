package com.github.monsterhxw.ch12.server;

import com.github.monsterhxw.ch12.proto.PacketDecoder;
import com.github.monsterhxw.ch12.proto.PacketEncoder;
import com.github.monsterhxw.ch12.server.handler.LoginRequestHandler;
import com.github.monsterhxw.ch12.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import static com.github.monsterhxw.ch12.util.ThreadUtil.getThreadName;

public class NettyServer {

    private static final int port = 8080;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) throws Exception {
                        System.out.println(getThreadName("Server") + "init server(listen) socket: " + ch);
                    }
                })
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName("Server") + "init client socket: " + ch);
                        ch.pipeline()
                                .addLast(new PacketDecoder())
                                .addLast(new LoginRequestHandler())
                                .addLast(new MessageRequestHandler(bossGroup, workerGroup))
                                .addLast(new PacketEncoder());
                    }
                });

        bind(serverBootstrap, port);
    }


    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        if (port <= 65_535) {
            serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    boolean success = future.isSuccess();
                    if (success) {
                        System.out.println(getThreadName("Server") + "bind [" + port + "] success!");
                    } else {
                        System.out.println(getThreadName("Server") + "bind [" + port + "] failed!");
                        bind(serverBootstrap, port + 1);
                    }
                }
            });
        }
    }
}